package com.mcnedward.keepfit.activity.fragment;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.utils.ActivityCode;
import com.mcnedward.keepfit.utils.Code;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Action;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;
import com.mcnedward.keepfit.view.AddGoalView;

import java.util.Calendar;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalOfDayFragment extends BaseFragment {
    private static final String TAG = "GoalOfDayFragment";

    public static boolean editable = false;

    private Context context;
    private IGoalRepository goalRepository;

    private int stepAmount = 10;
    private static Goal goalOfDay;

    private AddGoalView addGoalView;
    private TextView goalOfDayStepGoal;
    private TextView goalOfDayName;
    private static EditText editGoalOfDayStepAmount;
    private static ImageView imgDecrement;
    private static ImageView imgIncrement;
    private FloatingActionButton actionButton;
    private static TextView txtEditDate;
    private ProgressBar progressBar;
    private RelativeLayout content;

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
        editGoalOfDayStepAmount = (EditText) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);
        txtEditDate = (TextView) view.findViewById(R.id.edit_date);
        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        content = (RelativeLayout) view.findViewById(R.id.content);
        actionButton = (FloatingActionButton) getActivity().findViewById(R.id.fab);

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
        progressBar.setMax(goalOfDay.getStepGoal());
        progressBar.setProgress(goalOfDay.getStepAmount());
        toggleContent(true);
    }

    private void changeStepAmount(boolean up) {
        String currentStepsString = editGoalOfDayStepAmount.getText().toString();
        int currentSteps = 0;
        if (!currentStepsString.equals(""))
            currentSteps = Integer.parseInt(currentStepsString);
        if (up)
            currentSteps += stepAmount;
        else
            currentSteps -= stepAmount;
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
            actionButton.show();
        } else {
            content.setVisibility(View.GONE);
            addGoalView.setVisibility(View.VISIBLE);
            actionButton.hide();
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

    public static void toggleEditMode(boolean isEditMode, String date) {
        if (isEditMode) {
            txtEditDate.setVisibility(View.VISIBLE);
            txtEditDate.setText("Editing for: " + date);
        } else {
            txtEditDate.setVisibility(View.GONE);
        }
    }

}
