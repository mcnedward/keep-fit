package com.mcnedward.keepfit.activity.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.HistoryRepository;
import com.mcnedward.keepfit.repository.IHistoryRepository;
import com.mcnedward.keepfit.repository.loader.IGoalRepository;
import com.mcnedward.keepfit.utils.Extension;

import java.util.Random;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalOfDayFragment extends Fragment {
    private final static String TAG = "GoalOfDayFragment";
    public static final int LOADER_ID = new Random().nextInt(1000);

    private static Context context;

    private IGoalRepository goalRepository;
    private IHistoryRepository historyRepository;

    private int stepAmount = 10;
    private static Goal goalOfDay;
    private static TextView goalOfDayName;
    private static EditText editGoalOfDayStepAmount;
    private static TextView goalOfDayStepGoal;
    private static ImageView imgDecrement;
    private static ImageView imgIncrement;
    private static ProgressBar progressBar;
    private RelativeLayout content;
    private LinearLayout message;

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

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GoalOfDayFragment newInstance(int sectionNumber) {
        return new GoalOfDayFragment();
    }

    private void updateProgress(int currentProgress, int maxProgress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", currentProgress, maxProgress);
        animation.setDuration(2500);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
    }

    private void initialize(View view) {
        context = view.getContext();
        goalRepository = new GoalRepository(context);
        historyRepository = new HistoryRepository(context);

        goalOfDayName = (TextView) view.findViewById(R.id.goal_of_day_name);
        editGoalOfDayStepAmount = (EditText) view.findViewById(R.id.goal_of_day_step_amount);
        goalOfDayStepGoal = (TextView) view.findViewById(R.id.goal_of_day_step_goal);
        // Clear focus from EditText
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        content = (RelativeLayout) view.findViewById(R.id.content);
        message = (LinearLayout) view.findViewById(R.id.message);

        initializeStepCountButtons(view);

        progressBar = (ProgressBar) view.findViewById(R.id.step_progress_bar);
        progressBar.setProgress(0);

        toggleContent(false);
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

    private void checkForGoalOfDay() {
        History history = historyRepository.getHistoryForCurrentDate();
        if (history == null) return;
        Goal goalOfDay = history.getGoal();
        if (goalOfDay != null) {
            updateGoalOfDay(goalOfDay);
            updateProgress(goalOfDay.getStepAmount(), goalOfDay.getStepGoal());
            toggleContent(true);
        }
    }

    private static void updateGoalOfDay(Goal goal) {
        goalOfDay = goal;
        goalOfDayName.setText(goalOfDay.getName());
        editGoalOfDayStepAmount.setText(String.valueOf(goalOfDay.getStepAmount()));
        goalOfDayStepGoal.setText(String.valueOf(goalOfDay.getStepGoal()));
        progressBar.setMax(goalOfDay.getStepGoal());
        progressBar.setProgress(goalOfDay.getStepAmount());
    }

    public void changeStepAmount(boolean up) {
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

    private void toggleEditable(boolean edit) {
        editGoalOfDayStepAmount.setEnabled(edit);
        if (edit) {
            editGoalOfDayStepAmount.setTextColor(ContextCompat.getColor(context, R.color.Black));
            imgDecrement.setVisibility(View.VISIBLE);
            imgIncrement.setVisibility(View.VISIBLE);
        } else {
            editGoalOfDayStepAmount.setTextColor(ContextCompat.getColor(context, R.color.Gray));
            imgDecrement.setVisibility(View.GONE);
            imgIncrement.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_edit) {
            toggleEditable(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
