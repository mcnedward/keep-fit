package com.mcnedward.keepfit.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.algorithm.AlgorithmService;
import com.mcnedward.keepfit.algorithm.IAlgorithm;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.thread.BaseThread;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public class GoalValuesView extends LinearLayout {
    private static final String TAG = "StepValueItem";

    private Context context;
    private IGoalRepository goalRepository;
    private Goal goal;

    private TextView txtGoalOfDayStepGoal;
    private TextView txtGoalOfDayUnit;
    private ImageView imgDecrement;
    private ImageView imgIncrement;
    private Spinner spinUnit;
    private Spinner spinValue;

    // Edit Mode stuff
    private EditText editGoalOfDayStepAmount;
    private TextView txtAlgorithmStepAmount;
    private EditText editGoalOfDayAmount;
    private TextView txtGoalOfDaySlash;
    private Spinner spinGoalOfDayUnit;
    private LinearLayout stepContainer;
    private ColorStateList textColors;

    private GoalAlgorithmThread algorithmThread;

    public GoalValuesView(Context context) {
        super(context);
        initialize(context);
    }

    public GoalValuesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        inflate(context, R.layout.view_goal_values, this);
        this.context = context;
        goalRepository = new GoalRepository(context);
        algorithmThread = new GoalAlgorithmThread();
        AlgorithmService.setGoalValuesView(this);

        txtGoalOfDayStepGoal = (TextView) findViewById(R.id.goal_of_day_step_goal);
        txtGoalOfDayUnit = (TextView) findViewById(R.id.goal_of_day_unit);
        textColors = txtGoalOfDayUnit.getTextColors();

        initializeEditModeViews();
        initializeGoalOfDayStepAmount();
        initializeStepCountButtons();
    }

    private void initializeEditModeViews() {
        txtGoalOfDaySlash = (TextView) findViewById(R.id.goal_of_day_slash);
        editGoalOfDayAmount = (EditText) findViewById(R.id.edit_goal_of_day_step_goal);
        editGoalOfDayAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newGoalAmount = v.getText().toString();
                if (newGoalAmount.equals("")) return false;
                double goalAmount = Double.parseDouble(newGoalAmount);
                if (goalAmount == goal.getStepGoal()) return false;
                goal.setStepGoal(goalAmount);
                try {
                    goalRepository.update(goal);
                } catch (EntityDoesNotExistException e) {
                    e.printStackTrace();
                    return false;
                }
                Extension.broadcastUpdateGoalOfDay(goal, context);
                return true;
            }
        });

        spinGoalOfDayUnit = new Spinner(context);
        List<String> units = new ArrayList<>();
        for (Unit u : Unit.values())
            units.add(u.abbreviation);
        final ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, units);
        spinGoalOfDayUnit.setAdapter(adapter);
        spinGoalOfDayUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Unit newUnit = Unit.getByAbbreviation(adapter.getItem(position));
                if (newUnit == goal.getUnit()) return;
                goal.setUnit(newUnit);
                try {
                    goalRepository.update(goal);
                } catch (EntityDoesNotExistException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        stepContainer = (LinearLayout) findViewById(R.id.step_container);
    }

    private void initializeGoalOfDayStepAmount() {
        editGoalOfDayStepAmount = (EditText) findViewById(R.id.goal_of_day_step_amount);
        editGoalOfDayStepAmount.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    double changeAmount = Double.parseDouble(v.getText().toString());
                    changeAmount = Unit.convert((Unit) spinUnit.getSelectedItem(), goal.getUnit(), changeAmount);
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

        txtAlgorithmStepAmount = (TextView) findViewById(R.id.algorithm_goal_of_day_step_amount);
    }

    private void initializeStepCountButtons() {
        imgDecrement = (ImageView) findViewById(R.id.step_counter_decrement);
        imgDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(false);
            }
        });
        Extension.setRippleBackground(imgDecrement, context);

        imgIncrement = (ImageView) findViewById(R.id.step_counter_increment);
        imgIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(true);
            }
        });
        Extension.setRippleBackground(imgIncrement, context);
    }

    private void changeStepAmount(boolean up) {
        double currentSteps = getCurrentStepAmountFromEditText();
        // Convert the stepAmount value according to the selected unit
        double stepAmount = Unit.convert((Unit) spinUnit.getSelectedItem(), goal.getUnit(), (Integer) spinValue.getSelectedItem());
        if (up)
            currentSteps += stepAmount;
        else
            currentSteps -= stepAmount;
        updateGoalOfDayStepAmount(currentSteps);
    }

    /**
     * Mainly used for converting steps from the algorithm into steps for the goal.
     *
     * @param amount The amount of steps counted by the algorithm.
     */
    private void changeStepAmount(double amount) {
        double currentSteps = getCurrentStepAmountFromEditText();
        // Convert from steps to meters first
        double stepAmount = amount;

        Unit selectedUnit = (Unit) spinUnit.getSelectedItem();
        if (selectedUnit != Unit.STEP) {
            // If the selected unit is not steps, convert the algorithm steps to meters first, then those meters to the selected unit
            stepAmount = Unit.convert(Unit.STEP, Unit.METER, stepAmount);
            stepAmount = Unit.convert(Unit.METER, selectedUnit, stepAmount);
        }

        currentSteps += stepAmount;
        updateGoalOfDayStepAmount(currentSteps);
    }

    private double getCurrentStepAmountFromEditText() {
        String currentStepsString = editGoalOfDayStepAmount.getText().toString();
        double currentSteps = 0;
        if (!currentStepsString.equals(""))
            currentSteps = Double.parseDouble(currentStepsString);
        return currentSteps;
    }

    private void updateGoalOfDayStepAmount(double amount) {
        if (goal == null) return;
        goal.setStepAmount(amount);
        try {
            goalRepository.update(goal);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        Extension.broadcastUpdateGoalOfDay(goal, context);
    }

    public void updateGoalOfDay(Goal goal) {
        this.goal = goal;

        editGoalOfDayStepAmount.setText(String.valueOf(Unit.format(goal.getStepAmount())));
        txtGoalOfDayStepGoal.setText(String.valueOf(goal.getStepGoal()));
        txtGoalOfDayUnit.setText(goal.getUnit().abbreviation);

        if (goal.isGoalReached()) {
            txtGoalOfDaySlash.setTextColor(ContextCompat.getColor(context, R.color.Gold));
            txtGoalOfDayStepGoal.setTextColor(ContextCompat.getColor(context, R.color.Gold));
            txtGoalOfDayUnit.setTextColor(ContextCompat.getColor(context, R.color.Gold));

            editGoalOfDayStepAmount.setTextColor(ContextCompat.getColor(context, R.color.Gold));
            editGoalOfDayAmount.setTextColor(ContextCompat.getColor(context, R.color.Gold));
        } else {
            txtGoalOfDaySlash.setTextColor(textColors);
            txtGoalOfDayStepGoal.setTextColor(textColors);
            txtGoalOfDayUnit.setTextColor(textColors);

            editGoalOfDayStepAmount.setTextColor(textColors);
            editGoalOfDayAmount.setTextColor(textColors);
        }
    }

    public void toggleEditMode(boolean isEditMode) {
        if (isEditMode) {
            editGoalOfDayAmount.setVisibility(View.VISIBLE);
            editGoalOfDayAmount.setText(String.valueOf(goal.getStepGoal()));
            txtGoalOfDayStepGoal.setVisibility(View.GONE);

            stepContainer.removeView(txtGoalOfDayUnit);
            stepContainer.addView(spinGoalOfDayUnit);
        } else {
            editGoalOfDayAmount.setVisibility(View.GONE);
            txtGoalOfDayStepGoal.setVisibility(View.VISIBLE);

            stepContainer.removeView(spinGoalOfDayUnit);
            stepContainer.addView(txtGoalOfDayUnit);
        }
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public void setSpinUnit(Spinner spinUnit) {
        this.spinUnit = spinUnit;
    }

    public void setSpinValue(Spinner spinValue) {
        this.spinValue = spinValue;
    }

    public void notifyAlgorithmRunning(boolean running) {
        if (running) {
            algorithmThread.startThread();
        } else {
            algorithmThread.pauseThread();
        }
    }

    public class GoalAlgorithmThread extends BaseThread {

        private boolean algorithmRunning, needsToShow;

        @Override
        protected void doRunAction() {
            // Sleep for 100 milliseconds
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            post(new Runnable() {
                @Override
                public void run() {
                    // Update UI here
                    IAlgorithm algorithm = AlgorithmService.getAlgorithm();
                    if (algorithm == null) return;
                    if (algorithmRunning) {
                        if (needsToShow) {
                            txtAlgorithmStepAmount.setVisibility(VISIBLE);
                            editGoalOfDayStepAmount.setVisibility(GONE);
                            imgIncrement.setVisibility(GONE);
                            imgDecrement.setVisibility(GONE);
                            needsToShow = false;
                        }
                        if (goal == null) return;

                        double algorithmStepCount = algorithm.getStepCount();
                        changeStepAmount(algorithmStepCount);
                        txtAlgorithmStepAmount.setText(String.valueOf(goal.getStepAmount()));
                    } else {
                        txtAlgorithmStepAmount.setVisibility(GONE);
                        editGoalOfDayStepAmount.setVisibility(VISIBLE);
                        imgIncrement.setVisibility(VISIBLE);
                        imgDecrement.setVisibility(VISIBLE);
                    }
                }
            });
        }

        @Override
        protected void doStartAction() {
            algorithmRunning = true;
            needsToShow = true;
        }

        @Override
        public void pauseThread() {
            algorithmRunning = false;
            needsToShow = true;
        }

        @Override
        protected void doStopAction() {
            algorithmRunning = false;
        }

    }

}
