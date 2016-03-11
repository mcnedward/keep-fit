package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.SettingView;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsActivity extends FragmentActivity {
    private static final String TAG = "SettingsActivity";

    public static Unit UNIT;

    private NumberPicker picker;
    private SettingView viewTestMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        initialize();
    }

    private void initialize() {
        initializeNumberPicker();
        initializeTestModeSetting();
    }

    private void initializeNumberPicker() {
        picker = (NumberPicker) findViewById(R.id.settings_picker);
        picker.setMaxValue(1000);
        picker.setMinValue(1);
        picker.setValue((int) Unit.METER_TO_STEP);
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Unit.setMeterToStep(newVal);
            }
        });
    }

    private void initializeTestModeSetting() {
        viewTestMode = (SettingView) findViewById(R.id.settings_test_mode);
        viewTestMode.setChecked(MainActivity.IS_TEST_MODE);
        final Activity activity = this;
        viewTestMode.setCheckBoxClickListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Extension.broadcastEditModeSwitch(isChecked, activity);
            }
        });
    }

    private void updateCurrentSetting(SettingView view) {
        UNIT = view.getUnit();
    }

    public void setUnit(Unit unit) {
        UNIT = unit;
    }
}
