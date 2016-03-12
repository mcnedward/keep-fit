package com.mcnedward.keepfit.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.NumberPicker;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.SettingView;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    public static Unit UNIT;

    private NumberPicker picker;
    private SettingView viewTestMode;
    private SettingView viewEditMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        initialize();
        setTitle(R.string.settings_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialize() {
        initializeNumberPicker();
        initializeTestModeSetting();
        initializeEditModeSetting();
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
                Extension.broadcastTestModeSwitch(isChecked, activity);
            }
        });
        viewTestMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewTestMode.setChecked(!viewTestMode.isChecked());
                Extension.broadcastTestModeSwitch(viewTestMode.isChecked(), activity);
            }
        });
    }

    private void initializeEditModeSetting() {
        viewEditMode = (SettingView) findViewById(R.id.settings_edit_mode);
        viewEditMode.setChecked(MainActivity.IS_EDIT_MODE);
        final Activity activity = this;
        viewEditMode.setCheckBoxClickListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Extension.broadcastEditModeSwitch(isChecked, activity);
            }
        });
        viewEditMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEditMode.setChecked(!viewEditMode.isChecked());
                Extension.broadcastTestModeSwitch(viewEditMode.isChecked(), activity);
            }
        });
    }

    public void setUnit(Unit unit) {
        UNIT = unit;
    }
}
