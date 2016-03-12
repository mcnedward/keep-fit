package com.mcnedward.keepfit.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.utils.Extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class TabSelectionView extends LinearLayout {
    private static final String TAG = "TabSelectionView";

    private Context context;
    private TextView txtTabNumber;
    private RadioButton radioButton;
    private FragmentCode code;

    public TabSelectionView(Context context) {
        super(context);
        initialize(context);
    }

    public TabSelectionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TabSelectionView,
                0, 0);

        String title = "";
        try {
            title = a.getString(R.styleable.TabSelectionView_tab_selection_title);
        } finally {
            a.recycle();
        }
        ((TextView) findViewById(R.id.tab_selection_title)).setText(title);
    }

    private void initialize(final Context context) {
        inflate(context, R.layout.view_tab_selection, this);
        View view = findViewById(R.id.tab_container);
        Extension.setRippleBackground(view, context);
        txtTabNumber = (TextView) findViewById(R.id.tab_selection_number);
        radioButton = (RadioButton) findViewById(R.id.tab_selection_radio);
    }

    public void setFragmentCode(FragmentCode code) {
        this.code = code;
        txtTabNumber.setText(String.valueOf(code.getCodeId()));
    }

    public void setFragmentCodeId(int codeId) {
        code.setCodeId(codeId);
        txtTabNumber.setText(String.valueOf(code.getCodeId()));
    }

    public int getFragmentCodeId() {
        return code.getCodeId();
    }

    public FragmentCode getFragmentCode() {
        return code;
    }

    public void setChecked(boolean checked) {
        radioButton.setChecked(checked);
    }

    public RadioButton getRadioButton() {
        return radioButton;
    }

}
