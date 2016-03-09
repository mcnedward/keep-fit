package com.mcnedward.keepfit.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.view.SettingView;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = "SettingsActivity";

    public static Unit UNIT;
    private SettingView currentSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initialize();
    }

    private void initialize() {
        currentSetting = (SettingView) findViewById(R.id.meters_view);
        updateCurrentSetting(currentSetting);
    }

    private void updateCurrentSetting(SettingView view) {
        UNIT = view.getUnit();
    }

    public void setUnit(Unit unit) {
        UNIT = unit;
    }
}