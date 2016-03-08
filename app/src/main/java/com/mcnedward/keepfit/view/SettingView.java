package com.mcnedward.keepfit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingView extends LinearLayout {
    private static final String TAG = "SettingView";

    private Context context;
    private TextView txtSettingName;
    private RadioButton radioButton;
    private Unit unit;

    public SettingView(Context context) {
        super(context);
        initialize(context);
    }

    public SettingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.SettingsView,
                0, 0);

        try {
            String title = a.getString(R.styleable.SettingsView_setting_title);
            int unitId = a.getInteger(R.styleable.SettingsView_setting_unit, 0);
            unit = Unit.getById(unitId);
            txtSettingName.setText(title);
        } finally {
            a.recycle();
        }
    }

    private void initialize(Context context) {
        inflate(context, R.layout.view_setting, this);
        txtSettingName = (TextView) findViewById(R.id.setting_name);

        radioButton = (RadioButton) findViewById(R.id.radio_button);
    }

    public Unit getUnit() {
        return unit;
    }

    public void toggleRadio(boolean checked) {
        radioButton.setChecked(checked);
    }
}
