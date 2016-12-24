package com.whygraphics.multilineradiogroup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.Toast;

public class MainActivity extends Activity {

    // the layout
    private MultiLineRadioGroup mMultiLineRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // finds the layout
        mMultiLineRadioGroup = (MultiLineRadioGroup) findViewById(R.id.main_activity_multi_line_radio_group);

        // sets the listener
        mMultiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MultiLineRadioGroup group, RadioButton button) {
                Toast.makeText(MainActivity.this,
                        button.getText() + " was clicked",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
