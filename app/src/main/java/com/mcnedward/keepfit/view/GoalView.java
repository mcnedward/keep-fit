package com.mcnedward.keepfit.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.Plot;
import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.adapter.GoalListAdapter;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

/**
 * Created by Edward on 1/31/2016.
 */
public class GoalView extends RelativeLayout {

    private Context context;
    private GoalRepository repository;

    private Goal goal;
    private GoalListAdapter adapter;

    private TextView txtGoalName;
    private ImageView imgIsGoalOfDay;
    private ImageView imgGoalDelete;
    private ProgressBar progressBar;

    // Goal progress stuff
    private TextView txtGoalSteps;
    private TextView txtGoalAmount;
    private View goalProgressContainer;

    public GoalView(Goal goal, Context context) {
        super(context);
        repository = new GoalRepository(context);
        this.goal = goal;
        this.context = context;
        initialize();
    }

    public GoalView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        inflate(context, R.layout.item_goal, this);

        txtGoalName = (TextView) findViewById(R.id.list_goal_name);
        if (goal != null)
            txtGoalName.setText(goal.getName());

        imgIsGoalOfDay = (ImageView) findViewById(R.id.list_goal_isGoal_of_day);
        Extension.setRippleBackground(imgIsGoalOfDay, context);
        imgIsGoalOfDay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                repository.setGoalOfDay(goal);
                Extension.broadcastUpdateGoalOfDay(goal, context);
                Toast.makeText(getContext(), String.format("Set %s as the goal of the day!", goal.getName()), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged(true);
            }
        });

        imgGoalDelete = (ImageView) findViewById(R.id.list_goal_delete);
        Extension.setRippleBackground(imgGoalDelete, context);
        imgGoalDelete.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (adapter != null) {
                    try {
                        repository.delete(goal);
                    } catch (EntityDoesNotExistException e) {
                        e.printStackTrace();
                        return false;
                    }
                    Extension.broadcastDeleteGoal(goal, context);
                    Toast.makeText(context, "Deleted " + goal.getName(), Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        imgGoalDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Long tap to delete " + goal.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.step_progress_bar);

        txtGoalSteps = (TextView) findViewById(R.id.text_goal_steps);
        txtGoalAmount = (TextView) findViewById(R.id.text_goal_amount);
        goalProgressContainer = findViewById(R.id.goal_progress_container);

        checkIfGoalOfDay();
        checkIfGoalComplete();
    }

    private void checkIfGoalOfDay() {
        imgIsGoalOfDay.setImageDrawable(goal.isGoalOfDay() ?
                        ContextCompat.getDrawable(context, R.drawable.star_on) :
                        ContextCompat.getDrawable(context, R.drawable.star_off)
        );
    }

    private void checkIfGoalComplete() {
        if (goal.isGoalReached()) {
            findViewById(R.id.goal_top_container).setBackgroundColor(ContextCompat.getColor(context, R.color.Gold));
            Extension.setRippleBackground(imgIsGoalOfDay, R.color.FireBrick, R.color.Gold, context);
            Extension.setRippleBackground(imgGoalDelete, R.color.FireBrick, R.color.Gold, context);
        } else {
            findViewById(R.id.goal_top_container).setBackgroundColor(ContextCompat.getColor(context, R.color.GhostWhite));
            Extension.setRippleBackground(imgIsGoalOfDay, context);
            Extension.setRippleBackground(imgGoalDelete, context);
        }
    }

    public void update(Goal goal, GoalListAdapter adapter) {
        this.goal = goal;
        this.adapter = adapter;
        txtGoalName.setText(goal.getName());

        txtGoalSteps.setText(String.valueOf(Unit.format(goal.getStepAmount())) + goal.getUnit().abbreviation);
        txtGoalAmount.setText(String.valueOf(Unit.format(goal.getStepGoal())) + goal.getUnit().abbreviation);

        progressBar.setMax((int) goal.getStepGoal());
        progressBar.setProgress((int) goal.getStepAmount());

        checkIfGoalOfDay();
        checkIfGoalComplete();
    }
}
