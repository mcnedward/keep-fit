package com.mcnedward.keepfit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.MainActivity;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingView extends LinearLayout {
    private static final String TAG = "SettingView";

    private Context context;
    private TextView txtSettingName;
    private TextView txtDescription;
    private CheckBox checkBox;
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
            String description = a.getString(R.styleable.SettingsView_setting_description);
            int unitId = a.getInteger(R.styleable.SettingsView_setting_unit, 0);
            unit = Unit.getById(unitId);
            txtSettingName.setText(title);
            txtDescription.setText(description);
        } finally {
            a.recycle();
        }
    }

    private void initialize(Context context) {
        inflate(context, R.layout.view_setting, this);
        View view = findViewById(R.id.setting_container);
        Extension.setRippleBackground(view, context);
        txtSettingName = (TextView) findViewById(R.id.setting_name);
        txtDescription = (TextView) findViewById(R.id.setting_description);
        checkBox = (CheckBox) findViewById(R.id.setting_check);
    }

    public void setCheckBoxClickListener(CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void setChecked(boolean checked) {
        checkBox.setChecked(checked);
    }

    public boolean isChecked() {
        return checkBox.isChecked();
    }

    public Unit getUnit() {
        return unit;
    }

}
