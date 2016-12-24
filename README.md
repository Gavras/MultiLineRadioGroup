# MultiLineRadioGroup
Multi Line Radio Group is a Radio Group layout to show radio buttons in more than one line.

## XML Attributes:
 
 XML Attributes:
 
 max_in_row:
 A non-negative number that represents the maximum radio buttons in a row,
 0 for all in one line.
 
 radio_buttons:
 String-array resource reference that represents the texts of the desired radio buttons.
 
 default_button:
 String that represents the text or the index of the radio button to be checked by default.
 The string should be in the following format:
 for text: "text:[text-of-button]" where text-of-button is the text of the button to check.
 for index: "index:[index-of-button]" where index-of-button is the index of the button to check.
 when the prefix omitted, "text:" inserted implicitly.

## Code Example

From XML:
```xml
<com.whygraphics.multilineradiogroup.MultiLineRadioGroup xmlns:multi_line_radio_group="http://schemas.android.com/apk/res-auto"
        android:id="@+id/main_activity_multi_line_radio_group"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        multi_line_radio_group:default_button="@string/button_2"
        multi_line_radio_group:max_in_row="3"
        multi_line_radio_group:radio_buttons="@array/radio_buttons" />
```
and in arrays.xml:
```xml
<string-array name="radio_buttons">
        <item>button_1</item>
        <item>button_2</item>
        <item>button_3</item>
        <item>button_4</item>
        <item>button_5</item>
</string-array>
```

In the activity:
```java
MultiLineRadioGroup mMultiLineRadioGroup = (MultiLineRadioGroup) findViewById(R.id.main_activity_multi_line_radio_group);

mMultiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MultiLineRadioGroup group, RadioButton button) {
                Toast.makeText(MainActivity.this,
                        button.getText() + " was clicked",
                        Toast.LENGTH_SHORT).show();
            }
});
```

Adding radio buttons programmatically:
```java
mMultiLineRadioGroup.addButtons("button to add 1", "button to add 2", "button to add 3");
```
