package com.whygraphics.multilineradiogroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Parcelable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MultiLineRadioGroupTest {

    @Test
    public void configChange() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);

        Parcelable parcelable = testObject.onSaveInstanceState();

        testObject.onRestoreInstanceState(parcelable);
    }

    @Test
    public void removeAllButtonsHappyPath() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        testObject.addButtons("button one");

        assertEquals("precondition, we have one button to remove", 1, testObject.getRadioButtonCount());

        testObject.removeAllButtons();
        assertEquals(0, testObject.getRadioButtonCount());
    }

    @Test
    public void removeRangeOfButtonsHappyPath() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        testObject.addButtons("button one", "button two", "button three");
        assertEquals("precondition, we have buttons", 3, testObject.getRadioButtonCount());

        testObject.removeButtons(1, 1);

        assertEquals(2, testObject.getRadioButtonCount());
        assertEquals("button one", testObject.getRadioButtonAt(0).getText().toString());
        assertEquals("button three", testObject.getRadioButtonAt(1).getText().toString());
    }

    @Test
    public void removeButtons_ThrowsExceptionIfStartIsNegative() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        testObject.addButtons("button one", "button two", "button three");
        assertEquals("precondition, we have buttons", 3, testObject.getRadioButtonCount());

        try {
            testObject.removeButtons(new Random().nextInt() * -1, 1);
            fail("expected exception");
        } catch (IllegalArgumentException expected) {
            assertEquals(3, testObject.getRadioButtonCount());
        }
    }

    @Test
    public void removeButtons_ThrowsExceptionIfStartIsBeyondSize() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        testObject.addButtons("button one", "button two", "button three");
        assertEquals("precondition, we have buttons", 3, testObject.getRadioButtonCount());

        try {
            testObject.removeButtons(testObject.getRadioButtonCount(), 1);
            fail("expected exception");
        } catch (IllegalArgumentException expected) {
            assertEquals(3, testObject.getRadioButtonCount());
        }
    }

    @Test
    public void removeRangOfZeroButtonsWhenNoButtons() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);

        testObject.removeButtons(new Random().nextInt(), 0);
    }

    @Test
    public void removeAllButtonsDoesNothingIfNoButtons() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);

        testObject.removeAllButtons();

        assertEquals(0, testObject.getRadioButtonCount());
    }

    @Test
    public void baseRadioGroupOnClickAgainDoesNotEmitEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        RadioGroup radioGroup = new RadioGroup(appContext);
        CountingOnCheckedChangeListener listener = new CountingOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        clickAgainDoesNotEmitEvent(appContext, radioGroup);

        assertEquals(1, listener.count);
    }

    @Test
    public void baseRadioGroupOnCheckChangeEmitOnlyCheckEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        RadioGroup radioGroup = new RadioGroup(appContext);
        CountingOnCheckedChangeListener listener = new CountingOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);
        final RadioButton secondButton = new RadioButton(appContext);
        secondButton.setText("second");
        secondButton.setId(R.id.multi_line_radio_group_default_table_row);
        radioGroup.addView(secondButton);

        clickButton(secondButton);
        clickButton(firstButton);

        assertEquals(2, listener.count);
        assertEquals(Arrays.asList(true, true), listener.buttonState);
        assertEquals(Arrays.asList("second", "first"), listener.buttonText);
    }

    @Test
    public void baseRadioGroupOnCheckChangeEmitsOnClearCheck() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final RadioGroup radioGroup = new RadioGroup(appContext);
        CountingOnCheckedChangeListener listener = new CountingOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);
        assertEquals(0, listener.count);

        clickButton(firstButton);
        assertEquals(1, listener.count);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                radioGroup.clearCheck();
            }
        });

        assertEquals(Arrays.asList(true, false), listener.buttonState);
        assertEquals(Arrays.asList("first", "first", null), listener.buttonText);
        assertEquals(3, listener.count);
    }

    @Test
    public void multiLineRadioGroupOnCheckedChangedEmitOnChecked() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup radioGroup = new MultiLineRadioGroup(appContext);
        MultilineOnCheckedChangeListener listener = new MultilineOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);

        radioGroup.check(R.id.multi_line_radio_group_default_radio_button);
        assertEquals(1, listener.count);
        assertEquals(Collections.singletonList("first"), listener.buttonText);

        radioGroup.clearCheck();
        assertEquals(2, listener.count);
        assertEquals(Arrays.asList("first", "first"), listener.buttonText);

        radioGroup.check("first");
        assertEquals(3, listener.count);
        assertEquals(Arrays.asList("first", "first", "first"),
                listener.buttonText);

        radioGroup.clearCheck();
        assertEquals(4, listener.count);
        assertEquals(Arrays.asList("first", "first", "first", "first"),
                listener.buttonText);

        radioGroup.checkAt(0);
        assertEquals(5, listener.count);
        assertEquals(Arrays.asList("first", "first", "first", "first", "first"),
                listener.buttonText);
    }

    @Test
    public void multiLIneRadioGroupOnCheckChangeEmitsOnClearCheck() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final MultiLineRadioGroup radioGroup = new MultiLineRadioGroup(appContext);
        MultilineOnCheckedChangeListener listener = new MultilineOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);
        assertEquals(0, listener.count);

        clickButton(firstButton);
        assertEquals(1, listener.count);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                radioGroup.clearCheck();
            }
        });

        assertEquals(Arrays.asList("first", "first"), listener.buttonText);
        assertEquals(Arrays.asList(true, false), listener.buttonState);
        assertEquals(2, listener.count);
    }

    @Test
    public void multiLineRadioGroupOnCheckChangeEmitOnlyCheckEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup radioGroup = new MultiLineRadioGroup(appContext);
        MultilineOnCheckedChangeListener listener = new MultilineOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);
        final RadioButton secondButton = new RadioButton(appContext);
        secondButton.setText("second");
        secondButton.setId(R.id.multi_line_radio_group_default_table_row);
        radioGroup.addView(secondButton);

        clickButton(secondButton);
        clickButton(firstButton);

        assertEquals(2, listener.count);
        assertEquals(Arrays.asList("second", "first"), listener.buttonText);
    }

    @Test
    public void multiLineRadioGroupOnClickAgainDoesNotEmitEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup radioGroup = new MultiLineRadioGroup(appContext);
        MultilineOnCheckedChangeListener onCheckedChangeListener = new MultilineOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        clickAgainDoesNotEmitEvent(appContext, radioGroup);

        assertEquals(1, onCheckedChangeListener.count);
        assertEquals(Collections.singletonList("second"), onCheckedChangeListener.buttonText);
    }

    @Test
    public void onClickButtonCallsCustomOnClickListener() throws InterruptedException {
        final AtomicInteger clickCounter = new AtomicInteger(0);
        Context appContext = InstrumentationRegistry.getTargetContext();
        final MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        final RadioButton radioButton = new RadioButton(appContext);
        testObject.addView(radioButton);
        testObject.setOnClickListener(new MultiLineRadioGroup.OnClickListener() {
            @Override
            public void onClick(ViewGroup group, RadioButton button) {
                clickCounter.incrementAndGet();
                assertSame(testObject, group);
                assertSame(radioButton, button);
            }
        });

        clickButton(radioButton);

        assertEquals(1, clickCounter.get());
        assertTrue(radioButton.isChecked());
    }

    @Test
    public void clearCheckWhenNoListenerDoesNotCrash() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        final RadioButton radioButton = new RadioButton(appContext);
        testObject.addView(radioButton);

        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                radioButton.performClick();
                testObject.clearCheck();
            }
        });
    }

    @Test
    public void clearCheckWhenNoButtonsDoesNotCrash() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        final RadioButton radioButton = new RadioButton(appContext);

        testObject.clearCheck();
    }

    @Test
    public void clearCheckWhenButtonButNotCheckedDoesNothing() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final MultiLineRadioGroup testObject = new MultiLineRadioGroup(appContext);
        final RadioButton radioButton = new RadioButton(appContext);
        testObject.addView(radioButton);

        testObject.clearCheck();
    }

    @SuppressLint("SetTextI18n")
    private void clickAgainDoesNotEmitEvent(Context appContext, RadioGroup radioGroup) throws InterruptedException {
        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
        radioGroup.addView(firstButton);
        final RadioButton secondButton = new RadioButton(appContext);
        secondButton.setText("second");
        secondButton.setId(R.id.multi_line_radio_group_default_table_row);
        radioGroup.addView(secondButton);

        clickButton(secondButton);
        clickButton(secondButton);
    }

    private void clickButton(final RadioButton radioButton) throws InterruptedException {
        InstrumentationRegistry.getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                radioButton.performClick();
            }
        });
    }

    private static class CountingOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        private int count;
        private List<String> buttonText = new ArrayList<>();
        private List<Boolean> buttonState = new ArrayList<>();

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId > 0) {
                RadioButton button = group.findViewById(checkedId);
                buttonText.add(button.getText().toString());
                buttonState.add(button.isChecked());
            } else {
                buttonText.add(null);
            }
            ++count;
        }
    }

    private static class MultilineOnCheckedChangeListener implements MultiLineRadioGroup.OnCheckedChangeListener {

        private int count;
        private List<String> buttonText = new ArrayList<>();
        private List<Boolean> buttonState = new ArrayList<>();

        @Override
        public void onCheckedChanged(ViewGroup group, RadioButton button) {
            ++count;
            buttonText.add(button.getText().toString());
            buttonState.add(button.isChecked());
        }
    }
}
