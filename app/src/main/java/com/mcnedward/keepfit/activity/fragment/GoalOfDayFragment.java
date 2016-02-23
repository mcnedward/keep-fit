package com.mcnedward.keepfit.activity.fragment;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.utils.ActivityCode;
import com.mcnedward.keepfit.utils.Code;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.Calendar;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalOfDayFragment extends BaseGoalListFragment {
    private static final String TAG = "GoalOfDayFragment";

    public static boolean editable = false;

    private int stepAmount = 10;
    private Goal goalOfDay;
    private TextView goalOfDayStepGoal;
    private TextView goalOfDayName;
    private static EditText editGoalOfDayStepAmount;
    private static TextView goalDate;
    private static ImageView imgDecrement;
    private static ImageView imgIncrement;
    private static Button btnEditGoal;
    private ProgressBar progressBar;
    private RelativeLayout content;
    private RelativeLayout message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        initialize(view);
        initializeFAB();
        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_goal_of_day;
    }

    @Override
    protected void itemClickAction(Goal goal) {
        Toast.makeText(getContext(), "Long tap to set this as the active goal!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void itemLongClickAction(Goal goal) {
        historyRepository.updateGoalOfDay(goal);
        updateGoalOfDay(goal);
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

    private void updateGoalOfDayDate(Calendar calendar) {
        String timestamp = Extension.getTimestamp(calendar.getTime());
        goalOfDay.setCreatedOn(timestamp);
        try {
            goalRepository.update(goalOfDay);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
    }

    private void initializeEditButton(View view) {
        btnEditGoal = (Button) view.findViewById(R.id.btn_edit_goal);
        btnEditGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentSteps = Integer.valueOf(editGoalOfDayStepAmount.getText().toString());
                goalOfDay.setStepAmount(currentSteps);
                try {
                    goalRepository.update(goalOfDay);
                    History history = historyRepository.getHistoryForGoal(goalOfDay);
                    String newGoalDate = Extension.getDateDBFormat(goalOfDay.getCreatedOn());
                    String oldGoalDate = Extension.getDateDBFormat(history.getDate());
                    history.setDate(newGoalDate);
                    if (Integer.valueOf(newGoalDate) < Integer.valueOf(oldGoalDate))
                        history.setGoal(null);
                    historyRepository.update(history);
                    checkForGoalOfDay();
                } catch (EntityDoesNotExistException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void checkForGoalOfDay() {
        History history = historyRepository.getHistoryForCurrentDate();
        if (history == null) return;
        Goal goalOfDay = history.getGoal();
        if (goalOfDay == null) {
            toggleContent(false);
            return;
        }
        if (goalOfDay != null) {
            updateGoalOfDay(goalOfDay);
            updateProgress(goalOfDay.getStepAmount(), goalOfDay.getStepGoal());
        }
    }

    private void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        String date = Extension.getPrettyDate(goalOfDay.getCreatedOn());
        goalOfDayName.setText(goalOfDay.getName());
        editGoalOfDayStepAmount.setText(String.valueOf(goalOfDay.getStepAmount()));
        goalOfDayStepGoal.setText(String.valueOf(goalOfDay.getStepGoal()));
        goalDate.setText("Date: " + date);
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
        else {
            currentSteps -= stepAmount;
            if (currentSteps < 0)
                currentSteps = 0;
        }
        editGoalOfDayStepAmount.setText(String.valueOf(currentSteps));
    }

    public static void toggleEditable(boolean edit) {
        editGoalOfDayStepAmount.setEnabled(edit);
        if (edit) {
            editGoalOfDayStepAmount.setTextColor(ContextCompat.getColor(context, R.color.Black));
            imgDecrement.setVisibility(View.VISIBLE);
            imgIncrement.setVisibility(View.VISIBLE);
            goalDate.setVisibility(View.VISIBLE);
            btnEditGoal.setVisibility(View.VISIBLE);
        } else {
            editGoalOfDayStepAmount.setTextColor(ContextCompat.getColor(context, R.color.Gray));
            imgDecrement.setVisibility(View.GONE);
            imgIncrement.setVisibility(View.GONE);
            goalDate.setVisibility(View.GONE);
            btnEditGoal.setVisibility(View.GONE);
        }
    }

    private void toggleContent(boolean showContent) {
        if (showContent) {
            content.setVisibility(View.VISIBLE);
            message.setVisibility(View.GONE);
        } else {
            content.setVisibility(View.GONE);
            message.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Create the Floating Action Button
     */
    private void initializeFAB() {
        FloatingActionButton fab = (FloatingActionButton) ((Activity) context).findViewById(R.id.fab);
        final Activity activity = ((Activity) context);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Extension.startAddGoalPopup(goalOfDay, activity);
            }
        });
    }

    private void updateProgress(int currentProgress, int maxProgress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", currentProgress);
        animation.setDuration(2500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    private void initialize(View view) {
        goalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        editGoalOfDayStepAmount = (EditText) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);
        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        content = (RelativeLayout) view.findViewById(R.id.content);
        message = (RelativeLayout) view.findViewById(R.id.message);

        initializeStepCountButtons(view);
        initializeDateButton(view);
        initializeEditButton(view);

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);

        toggleContent(editable);
        checkForGoalOfDay();
        toggleEditable(false);
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

    private void initializeDateButton(View view) {
        goalDate = (TextView) view.findViewById(R.id.goal_date);
        Extension.setRippleBackground(goalDate, context);
        Calendar myCalendar = Calendar.getInstance();
        goalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Extension.getCalendar(goalOfDay.getCreatedOn());
                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateGoalOfDayDate(calendar);
                    }
                };
                new DatePickerDialog(context, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
