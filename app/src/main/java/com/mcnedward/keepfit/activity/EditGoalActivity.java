package com.mcnedward.keepfit.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.activity.fragment.MainContentFragment;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

/**
 * Created by Edward on 1/31/2016.
 */
public class EditGoalActivity extends AppCompatActivity {
    private static final String TAG = "EditGoalActivity";

    private TextView editGoalTitle;
    private EditText editGoalName;
    private EditText editGoalSteps;

    private GoalRepository repository;

    private Goal editGoal;
    private boolean isEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_goal);

        repository = new GoalRepository(this);
        editGoal = (Goal) getIntent().getSerializableExtra("goal");
        isEdit = getIntent().getBooleanExtra("isEdit", true);

        initialize();
    }

    private void initialize() {
        editGoalTitle = (TextView) findViewById(R.id.edit_goal_title);
        editGoalName = (EditText) findViewById(R.id.edit_goal_name);
        editGoalSteps = (EditText) findViewById(R.id.edit_goal_steps);
        Button btnAddGoal = (Button) findViewById(R.id.btn_edit_goal);

        if (isEdit) {
            editGoalTitle.setText("Edit " + editGoal.getName());
            btnAddGoal.setText("Edit");
            editGoalName.setText(editGoal.getName());
            editGoalSteps.setText(String.valueOf(editGoal.getStepGoal()));
        }

        btnAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit)
                    editGoal();
                else
                    addGoal();
            }
        });
    }

    private void editGoal() {
        String originalGoalName = editGoal.getName();
        int originalGoalSteps = editGoal.getStepGoal();
        String goalName = editGoalName.getText().toString();
        String goalSteps = editGoalSteps.getText().toString();

        if (goalName.equals("") || goalSteps.equals("")) {
            Toast.makeText(this, "You need to fill in everything!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (goalName.equals(originalGoalName) && Integer.valueOf(goalSteps) == originalGoalSteps) {
            Toast.makeText(this, "You need to change something!", Toast.LENGTH_SHORT).show();
            return;
        }
        editGoal.setName(goalName);
        editGoal.setStepGoal(Integer.valueOf(goalSteps));

        try {
            repository.update(editGoal);
        } catch (EntityDoesNotExistException e) {
            e.printStackTrace();
        }
        MainContentFragment.editGoal(editGoal);

        Toast.makeText(this, "Updated " + originalGoalName + " to " + goalName + "!", Toast.LENGTH_SHORT).show();

        finish();
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
        MainContentFragment.addGoal(goal);
        Toast.makeText(this, "Added " + goalName + "!", Toast.LENGTH_SHORT).show();

        editGoalName.setText("");
        editGoalSteps.setText("");

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}