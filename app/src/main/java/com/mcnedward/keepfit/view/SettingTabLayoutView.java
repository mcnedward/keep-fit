package com.mcnedward.keepfit.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.repository.FragmentCodeRepository;
import com.mcnedward.keepfit.repository.IFragmentCodeRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public class SettingTabLayoutView extends LinearLayout {
    private static final String TAG = "TabLayoutView";

    private Context context;
    private IFragmentCodeRepository repository;
    private TabSelectionView tabGoalOfDay;
    private TabSelectionView tabGoals;
    private TabSelectionView tabHistory;
    private TabSelectionView tabStatistics;
    private List<TabSelectionView> buttons;
    private NumberPicker picker;
    private boolean valuesChanged = false;

    public SettingTabLayoutView(Context context) {
        super(context);
        initialize(context);
    }

    public SettingTabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private boolean fromSwitch = false;

    private void initialize(final Context context) {
        inflate(context, R.layout.view_setting_tab_layout, this);
        repository = new FragmentCodeRepository(context);
        List<FragmentCode> fragmentCodes = repository.getFragmentCodesSorted();
        buttons = new ArrayList<>();

        tabGoalOfDay = (TabSelectionView) findViewById(R.id.tab_goal_of_day);
        tabGoals = (TabSelectionView) findViewById(R.id.tab_goals);
        tabHistory = (TabSelectionView) findViewById(R.id.tab_history);
        tabStatistics = (TabSelectionView) findViewById(R.id.tab_statistics);

        for (FragmentCode code : fragmentCodes) {
            switch (code.getCodeByTitle()) {
                case GOAL_OF_THE_DAY:
                    tabGoalOfDay.setFragmentCode(code);
                    break;
                case GOALS:
                    tabGoals.setFragmentCode(code);
                    break;
                case HISTORY:
                    tabHistory.setFragmentCode(code);
                    break;
                case STATISTICS:
                    tabStatistics.setFragmentCode(code);
                    break;
            }
        }

        buttons.add(tabGoalOfDay);
        buttons.add(tabGoals);
        buttons.add(tabHistory);
        buttons.add(tabStatistics);

        tabGoalOfDay.setChecked(true);
        picker = (NumberPicker) findViewById(R.id.tab_picker);
        picker.setMaxValue(fragmentCodes.size());
        picker.setMinValue(1);
        picker.setValue(tabGoalOfDay.getFragmentCodeId());

        initializeRadioButtonCheckEvents();
        initializePickerChangedEvent();
    }

    private void initializeRadioButtonCheckEvents() {
        for (final TabSelectionView button : buttons) {
            button.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    button.getRadioButton().setChecked(true);
                    picker.setValue(button.getFragmentCodeId());
                    for (TabSelectionView b : buttons) {
                        if (b != button)
                            b.getRadioButton().setChecked(false);
                    }
                }
            });
            final RadioButton radioButton = button.getRadioButton();
            radioButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    picker.setValue(button.getFragmentCodeId());
                    for (TabSelectionView b : buttons) {
                        if (b.getRadioButton() != radioButton)
                            b.getRadioButton().setChecked(false);
                    }
                }
            });
        }
    }

    private void initializePickerChangedEvent() {
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                // The values have been changed, so an app restart will have to be triggered
                valuesChanged = true;

                // Change the old value first
                TabSelectionView changedButton = null;
                for (TabSelectionView button : buttons) {
                    if (button.getFragmentCodeId() == oldVal) {
                        button.setFragmentCodeId(newVal);
                        changedButton = button;
                        break;
                    }
                }
                // Change the new value to the old one
                for (TabSelectionView button : buttons) {
                    if (button != changedButton && button.getFragmentCodeId() == newVal) {
                        button.setFragmentCodeId(oldVal);
                        break;
                    }
                }
            }
        });
    }

    public List<FragmentCode> getFragmentCodes() {
        List<FragmentCode> codes = new ArrayList<>();
        for (TabSelectionView button : buttons) {
            codes.add(button.getFragmentCode());
        }
        return codes;
    }

    public boolean valuesChanged() {
        return valuesChanged;
    }
}
