package com.mcnedward.keepfit.activity.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.adapter.EnumAdapter;
import com.mcnedward.keepfit.utils.enums.ActivityCode;
import com.mcnedward.keepfit.utils.enums.Code;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.AddGoalView;

import java.util.Arrays;

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
    private TextView goalOfDayStepGoal;
    private TextView goalOfDayName;
    private EditText editGoalOfDayName;
    private TextView goalOfDayUnit;
    private EditText editGoalOfDayStepAmount;
    private ImageView imgDecrement;
    private ImageView imgIncrement;
    private TextView txtTestDate;
    private Spinner spinUnit;
    private Spinner spinValue;
    private EnumAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout content;
    private FloatingActionButton fab;

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
        switch (requestCode) {
            case ActivityCode.ADD_GOAL_POPUP:
                if (resultCode == Code.RESULT_OK.id()) {
                    Goal goal = (Goal) data.getSerializableExtra("goal");
                    updateGoalOfDay(goal);
                }
                break;
        }
    }

    @Override
    protected void initialize(View view) {
        context = view.getContext();
        goalRepository = new GoalRepository(view.getContext());
        addGoalView = (AddGoalView) view.findViewById(R.id.add_goal_view);
        goalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);
        txtTestDate = (TextView) view.findViewById(R.id.test_date);
        goalOfDayUnit = (TextView) view.findViewById(R.id.goal_of_day_unit);

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);

        initializeEditGoalOfDayName(view);
        initializeEditGoalOfDayStepAmount(view);
        initializeSpinners(view);
        initializeFAB(view);
        initializeStepCountButtons(view);

        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        content = (RelativeLayout) view.findViewById(R.id.content);

        toggleContent(editable);
        checkForGoalOfDay();
    }

    private void initializeEditGoalOfDayName(View view) {
        editGoalOfDayName = (EditText) view.findViewById(R.id.edit_goal_of_day_name);
        editGoalOfDayName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });
    }

    private void initializeEditGoalOfDayStepAmount(View view) {
        editGoalOfDayStepAmount = (EditText) view.findViewById(R.id.goal_of_day_step_amount);
        editGoalOfDayStepAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    double changeAmount = Double.parseDouble(v.getText().toString());
                    changeAmount = Unit.convert((Unit) spinUnit.getSelectedItem(), goalOfDay.getUnit(), changeAmount);
                    updateGoalOfDayStepAmount(changeAmount);
                    View view = ((Activity) context).getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

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
                }
            });
        }
    }

    @Override
    protected void addGoalActionReceived(Goal goal) {
        updateGoalOfDay(goal);
        toggleContent(true);
    }

    @Override
    protected void deleteGoalActionReceived(Goal goal) {
    }

    @Override
    protected void updateGoalOfDayActionReceived(Goal goal) {
        updateGoalOfDay(goal);
    }

    @Override
    protected void updateGoalAmountActionReceived(Goal goal) {

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

    private void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        goalOfDayName.setText(goalOfDay.getName());
        editGoalOfDayStepAmount.setText(String.valueOf(Unit.format(goalOfDay.getStepAmount())));
        goalOfDayStepGoal.setText(String.valueOf(goalOfDay.getStepGoal()));
        goalOfDayUnit.setText(goalOfDay.getUnit().abbreviation);
        progressBar.setMax((int) goalOfDay.getStepGoal());
        progressBar.setProgress((int) goalOfDay.getStepAmount());
        toggleContent(true);
    }

    private void changeStepAmount(boolean up) {
        String currentStepsString = editGoalOfDayStepAmount.getText().toString();
        double currentSteps = 0;
        if (!currentStepsString.equals(""))
            currentSteps = Double.parseDouble(currentStepsString);
        // Convert the stepAmount value according to the selected unit
        double stepAmount = Unit.convert((Unit) spinUnit.getSelectedItem(), goalOfDay.getUnit(), (Integer) spinValue.getSelectedItem());
        if (up)
            currentSteps += stepAmount;
        else
            currentSteps -= stepAmount;
        updateGoalOfDayStepAmount(currentSteps);
    }

    private void updateGoalOfDayStepAmount(double amount) {
        goalOfDay.setStepAmount(amount);
        try {
            goalRepository.update(goalOfDay);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        Extension.broadcastUpdateGoalOfDay(goalOfDay, context);
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

    private void updateProgress(int currentProgress, int maxProgress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", currentProgress);
        animation.setDuration(2500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    private void initializeStepCountButtons(View view) {
        imgDecrement = (ImageView) view.findViewById(R.id.step_counter_decrement);
        imgDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(false);
            }
        });
        Extension.setRippleBackground(imgDecrement, context);

        imgIncrement = (ImageView) view.findViewById(R.id.step_counter_increment);
        imgIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(true);
            }
        });
        Extension.setRippleBackground(imgIncrement, context);
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
            editGoalOfDayName.setVisibility(View.VISIBLE);
            editGoalOfDayName.setText(goalOfDayName.getText());
            goalOfDayName.setVisibility(View.GONE);
        } else {
            editGoalOfDayName.setVisibility(View.GONE);
            goalOfDayName.setVisibility(View.VISIBLE);
        }
    }

}
