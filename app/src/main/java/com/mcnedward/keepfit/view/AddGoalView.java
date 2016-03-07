package com.mcnedward.keepfit.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;

/**
 * Created by Edward on 3/7/2016.
 */
public class AddGoalView extends LinearLayout {
    private static final String TAG = "AddGoalView";

    private Context context;
    private GoalRepository repository;
    private Goal goal;
    private EditText editGoalName;
    private EditText editGoalSteps;

    public AddGoalView(Context context) {
        super(context);
        initialize(context);
    }

    public AddGoalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }
    private void initialize(Context context) {
        inflate(context, R.layout.view_add_goal, this);
        this.context = context;
        if (!isInEditMode())
            repository = new GoalRepository(context);

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
            Toast.makeText(context, "You need to fill in everything!", Toast.LENGTH_SHORT).show();
            return;
        }
        goal = new Goal(goalName, Integer.valueOf(goalSteps));

        try {
            repository.save(goal);
        } catch (EntityAlreadyExistsException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Added " + goalName + "!", Toast.LENGTH_SHORT).show();

        editGoalName.setText("");
        editGoalSteps.setText("");

        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        broadcastAddGoal();
    }

    public void broadcastAddGoal() {
        Intent intent = new Intent("addGoal");
        intent.putExtra("goal", goal);
        intent.putExtra("action", 1);
        context.sendBroadcast(intent);
    }

}
