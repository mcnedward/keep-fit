package com.mcnedward.keepfit.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.MainContentFragment;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.Code;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

/**
 * Created by Edward on 1/31/2016.
 */
public class AddGoalPopup extends Activity {
    private static final String TAG = "AddGoalPopup";

    private GoalRepository repository;

    private Goal goal;
    private EditText editGoalName;
    private EditText editGoalSteps;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_add_goal);
        repository = new GoalRepository(this);
        goal = (Goal) getIntent().getSerializableExtra("goal");
        initialize();
        initializeWindow();
    }

    private void initialize() {
        editGoalName = (EditText) findViewById(R.id.edit_goal_name);
        editGoalSteps = (EditText) findViewById(R.id.edit_goal_steps);
        (findViewById(R.id.btn_edit_goal)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGoal();
            }
        });
    }

    private void addGoal() {
        String goalName = editGoalName.getText().toString();
        String goalSteps = editGoalSteps.getText().toString();

        if (goalName.equals("") || goalSteps.equals("")) {
            Toast.makeText(this, "You need to fill in everything!", Toast.LENGTH_SHORT).show();
            return;
        }
        Goal goal = new Goal(goalName, Integer.valueOf(goalSteps));

        try {
            repository.save(goal);
        } catch (EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Added " + goalName + "!", Toast.LENGTH_SHORT).show();

        editGoalName.setText("");
        editGoalSteps.setText("");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        finish();
        Intent intent = new Intent();
        intent.putExtra("goal", goal);
        setResult(Code.RESULT_OK.id(), intent);
    }

    private void initializeWindow() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = (int) (dm.widthPixels * 0.8);
        int height = (int) (dm.heightPixels * 0.45);

        getWindow().setLayout(width, height);
    }

}
