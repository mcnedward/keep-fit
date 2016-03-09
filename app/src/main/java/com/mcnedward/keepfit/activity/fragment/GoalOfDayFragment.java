package com.mcnedward.keepfit.activity.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
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
import com.mcnedward.keepfit.utils.adapter.UnitAdapter;
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

    private int stepAmount = 10;
    private static Goal goalOfDay;
    public boolean editable = false;
    private Unit selectedUnit;

    private AddGoalView addGoalView;
    private TextView goalOfDayStepGoal;
    private TextView goalOfDayName;
    private TextView goalOfDayUnit;
    private EditText editGoalOfDayStepAmount;
    private ImageView imgDecrement;
    private ImageView imgIncrement;
    private TextView txtEditDate;
    private Spinner spinUnit;
    private UnitAdapter adapter;
    private ProgressBar progressBar;
    private RelativeLayout content;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_of_day, container, false);
        initialize(view);
        return view;
    }
    private void initializeUnitSpinner(View view) {
        spinUnit = (Spinner) view.findViewById(R.id.goal_of_day_spinner_unit);
        adapter = new UnitAdapter(context, android.R.layout.simple_spinner_item, Arrays.asList(Unit.values()));
        spinUnit.setAdapter(adapter);
        selectedUnit = (Unit) spinUnit.getSelectedItem();
        spinUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedUnit = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        editGoalOfDayStepAmount = (EditText) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);
        txtEditDate = (TextView) view.findViewById(R.id.edit_date);
        goalOfDayUnit = (TextView) view.findViewById(R.id.goal_of_day_unit);

        initializeUnitSpinner(view);

        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Extension.startAddGoalPopup(((Activity) context));
            }
        });

        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        content = (RelativeLayout) view.findViewById(R.id.content);

        initializeStepCountButtons(view);

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);

        toggleContent(editable);
        checkForGoalOfDay();

        registerReceivers();
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
    protected void editModeSwitchActionReceived(boolean isEditMode, String date) {
        toggleEditMode(isEditMode, date);
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
        editGoalOfDayStepAmount.setText(String.valueOf(goalOfDay.getStepAmount()));
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

        if (up)
            currentSteps += stepAmount;
        else
            currentSteps -= stepAmount;

        // Convert the amount according to the selected unit
//        currentSteps = Conversion.convert(selectedUnit, goalOfDay.getUnit(), currentSteps);

        goalOfDay.setStepAmount(currentSteps);
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
        txtEditDate.setVisibility(View.VISIBLE);
        txtEditDate.setText("Editing for: " + date);
        checkForGoalOfDay();
    }

    private void toggleEditMode(boolean isEditMode, String date) {
        if (isEditMode) {
            txtEditDate.setVisibility(View.VISIBLE);
            txtEditDate.setText("Editing for: " + date);
        } else {
            txtEditDate.setVisibility(View.GONE);
        }
    }

}
