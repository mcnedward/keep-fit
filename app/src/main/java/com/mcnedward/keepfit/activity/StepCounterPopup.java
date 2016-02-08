package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.MainContentFragment;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;

/**
 * Created by Edward on 1/31/2016.
 */
public class StepCounterPopup extends Activity {
    private final static String TAG = "StepCounterPopup";

    private Goal goal;
    private int stepAmount = 10;
    private ImageView imgDecrement;
    private EditText editStepCounter;
    private ImageView imgIncrement;
    private Button btnUpdate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_counter_popup_item);
        goal = (Goal) getIntent().getSerializableExtra("goal");
        initialize();
        initializeWindow();
    }

    private void initialize() {
        imgDecrement = (ImageView) findViewById(R.id.step_counter_decrement);
        imgDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(false);
            }
        });
        Extension.setRippleBackground(imgDecrement, this);

        editStepCounter = (EditText) findViewById(R.id.step_counter_edit);
        editStepCounter.setText(String.valueOf(goal.getStepAmount()));


        imgIncrement = (ImageView) findViewById(R.id.step_counter_increment);
        imgIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStepAmount(true);
            }
        });
        Extension.setRippleBackground(imgIncrement, this);

        btnUpdate = (Button) findViewById(R.id.btn_step_counter_update);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSteps();
            }
        });
    }

    public void changeStepAmount(boolean up) {
        String currentStepsString = editStepCounter.getText().toString();
        int currentSteps = 0;
        if (!currentStepsString.equals(""))
            currentSteps = Integer.parseInt(currentStepsString);
        if (up)
            currentSteps += stepAmount;
        else
            currentSteps -= stepAmount;
        editStepCounter.setText(String.valueOf(currentSteps));
    }

    private void updateSteps() {
        int steps = Integer.parseInt(editStepCounter.getText().toString());
        MainContentFragment.updateStepCounter(steps);
        finish();
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.6);
        int height = (int) (dm.heightPixels * 0.225);

        getWindow().setLayout(width, height);
    }

}