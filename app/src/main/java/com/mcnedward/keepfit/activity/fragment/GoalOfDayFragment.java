package com.mcnedward.keepfit.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.adapter.EnumAdapter;
import com.mcnedward.keepfit.utils.enums.Action;
import com.mcnedward.keepfit.utils.enums.Code;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.AddGoalView;
import com.mcnedward.keepfit.view.GoalValuesView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalOfDayFragment extends BaseFragment {
    private static final String TAG = "GoalOfDayFragment";

    private Context context;
    private IGoalRepository goalRepository;

    private static Integer[] VALUES = new Integer[]
            {
                    10, 20, 50, 100, 200, 500, 1000
            };

    private static Goal goalOfDay;
    public boolean editable = false;

    private AddGoalView addGoalView;
    private GoalValuesView goalValuesView;

    private EditText editGoalOfDayName;
    private TextView txtGoalOfDayName;
    private TextView txtTestDate;
    private Spinner spinUnit;
    private Spinner spinValue;
    private EnumAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout content;
    private FloatingActionButton fab;
    private ColorStateList textColors;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_of_day, container, false);
        initialize(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Action.ADD_GOAL_POPUP.id) {
            if (resultCode == Code.RESULT_OK.id()) {
                Goal goal = (Goal) data.getSerializableExtra("goal");
                updateGoalOfDay(goal);
                return;
            }
        }
    }

    @Override
    protected void initialize(View view) {
        context = view.getContext();
        goalRepository = new GoalRepository(view.getContext());
        addGoalView = (AddGoalView) view.findViewById(R.id.add_goal_view);

        txtTestDate = (TextView) view.findViewById(R.id.test_date);

        txtGoalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        textColors =  txtGoalOfDayName.getTextColors();

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);

        intializeEditGoalName(view);
        initializeSpinners(view);
        initializeFAB(view);

        goalValuesView = (GoalValuesView) view.findViewById(R.id.goal_values_view);
        goalValuesView.setSpinUnit(spinUnit);
        goalValuesView.setSpinValue(spinValue);

        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        content = (RelativeLayout) view.findViewById(R.id.content);

        toggleContent(editable);
        checkForGoalOfDay();
    }

    private void intializeEditGoalName(View view) {
        editGoalOfDayName = (EditText) view.findViewById(R.id.edit_goal_of_day_name);
        editGoalOfDayName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newGoalName = v.getText().toString();
                if (!newGoalName.equals(goalOfDay.getName())) {
                    goalOfDay.setName(newGoalName);
                    try {
                        goalRepository.update(goalOfDay);
                    } catch (EntityDoesNotExistException e) {
                        e.printStackTrace();
                    }
                }
                View view = ((Activity) context).getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
        });}

    private void initializeSpinners(View view) {
        spinUnit = (Spinner) view.findViewById(R.id.goal_of_day_spinner_unit);
        adapter = new EnumAdapter(context, android.R.layout.simple_spinner_item, Unit.getEnums());
        spinUnit.setAdapter(adapter);

        spinValue = (Spinner) view.findViewById(R.id.goal_of_day_spinner_value);
        ArrayAdapter<Integer> valuesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, Arrays.asList(VALUES));
        spinValue.setAdapter(valuesAdapter);
    }

    private void initializeFAB(View view) {
        if (!view.isInEditMode()) {
            fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Extension.startAddGoalPopup(((Activity) context));
                    setPauseReceiver(false);
                }
            });
        }
    }

    private void checkForGoalOfDay() {
        Goal goalOfDay = goalRepository.getGoalOfDay();
        if (goalOfDay == null) {
            toggleContent(false);
            return;
        }
        if (goalOfDay != null) {
            updateGoalOfDay(goalOfDay);
        }
    }

    private boolean progressBarColorChanged = false;
    private void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        txtGoalOfDayName.setText(goalOfDay.getName());
        toggleContent(true);

        if (goal.isGoalReached()) {
            txtGoalOfDayName.setTextColor(ContextCompat.getColor(context, R.color.Gold));
            editGoalOfDayName.setTextColor(ContextCompat.getColor(context, R.color.Gold));

            if (!progressBarColorChanged) {
                progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circle_progress_complete));
                progressBarColorChanged = true;
            }
        } else if (!goal.isGoalReached() && goal.switchColorsBack()){
            txtGoalOfDayName.setTextColor(textColors);
            editGoalOfDayName.setTextColor(textColors);

            progressBar.setProgressDrawable(ContextCompat.getDrawable(context, R.drawable.circle_progress));
            progressBarColorChanged = false;

            goal.setSwitchColors(false);
        }

        progressBar.setMax((int) goalOfDay.getStepGoal());
        progressBar.setProgress((int) goalOfDay.getStepAmount());

        goalValuesView.updateGoalOfDay(goal);
    }

    private void toggleContent(boolean showContent) {
        if (showContent) {
            content.setVisibility(View.VISIBLE);
            addGoalView.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.GONE);
            addGoalView.setVisibility(View.VISIBLE);
        }
    }

    private void toggleCalendarChange(String date) {
        txtTestDate.setVisibility(View.VISIBLE);
        txtTestDate.setText("Editing for: " + date);
        checkForGoalOfDay();
    }

    private void toggleTestMode(boolean isTestMode, String date) {
        if (isTestMode) {
            txtTestDate.setVisibility(View.VISIBLE);
            txtTestDate.setText("In Test Mode for: " + date);
        } else {
            txtTestDate.setVisibility(View.GONE);
        }
    }

    private void toggleEditMode(boolean isEditMode) {
        if (isEditMode) {
            txtGoalOfDayName.setVisibility(View.GONE);
            editGoalOfDayName.setVisibility(View.VISIBLE);
            editGoalOfDayName.setText(goalOfDay.getName());

            editGoalOfDayName.setVisibility(View.VISIBLE);
            editGoalOfDayName.setText(goalOfDay.getName());

            editGoalOfDayName.setVisibility(View.VISIBLE);
            editGoalOfDayName.setText(goalOfDay.getName());
        } else {
            txtGoalOfDayName.setVisibility(View.VISIBLE);

            editGoalOfDayName.setVisibility(View.GONE);
            editGoalOfDayName.setVisibility(View.GONE);

            // Update the goal of day if an edit mode is finished
            checkForGoalOfDay();
        }
        goalValuesView.toggleEditMode(isEditMode);
    }

    @Override
    protected void addGoalActionReceived(Goal goal) {
        updateGoalOfDay(goal);
        toggleContent(true);
    }

    @Override
    protected void deleteGoalActionReceived(Goal goal) {
        checkForGoalOfDay();
    }

    @Override
    protected void updateGoalOfDayActionReceived(Goal goal) {
        updateGoalOfDay(goal);
    }

    @Override
    protected void testModeSwitchActionReceived(boolean isTestMode, String date) {
        toggleTestMode(isTestMode, date);
    }

    @Override
    protected void editModeSwitchActionReceived(boolean isEditMode) {
        toggleEditMode(isEditMode);
    }

    @Override
    protected void calendarChangeActionReceived(String date) {
        toggleCalendarChange(date);
    }

    @Override
    protected void notifyAlgorithmRunning(boolean running) {
        Log.d(TAG, "running: " + running);
        goalValuesView.notifyAlgorithmRunning(running);
    }
}
