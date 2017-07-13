package com.whygraphics.multilineradiogroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class MultiLineRadioGroupTest {
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
    public void baseRadioGroupClickAgainDoesNotEmitEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        RadioGroup radioGroup = new RadioGroup(appContext);
        CountingOnCheckedChangeListener listener = new CountingOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(listener);

        clickAgainDoesNotEmitEvent(appContext, radioGroup);

        assertEquals(1, listener.count);
    }

    @Test
    public void multiLineRadioGroupClickAgainDoesNotEmitEvent() throws InterruptedException {
        Context appContext = InstrumentationRegistry.getTargetContext();
        MultiLineRadioGroup radioGroup = new MultiLineRadioGroup(appContext);
        MultilineOnCheckedChangeListener onCheckedChangeListener = new MultilineOnCheckedChangeListener();
        radioGroup.setOnCheckedChangeListener(onCheckedChangeListener);

        clickAgainDoesNotEmitEvent(appContext, radioGroup);

        assertEquals(1, onCheckedChangeListener.count);
    }

    @SuppressLint("SetTextI18n")
    private void clickAgainDoesNotEmitEvent(Context appContext, RadioGroup radioGroup) throws InterruptedException {
        final RadioButton firstButton = new RadioButton(appContext);
        firstButton.setText("first");
        firstButton.setId(R.id.multi_line_radio_group_default_radio_button);
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

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            ++count;
        }
    }

    private static class MultilineOnCheckedChangeListener implements MultiLineRadioGroup.OnCheckedChangeListener {
        private int count;

        @Override
        public void onCheckedChanged(ViewGroup group, RadioButton button) {
            ++count;
        }
    }
}