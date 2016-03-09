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

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.adapter.GoalListAdapter;
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
    private ImageView imgGoalEdit;
    private ImageView imgGoalDelete;
    private ProgressBar progressBar;

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

        imgGoalEdit = (ImageView) findViewById(R.id.list_goal_edit);
        Extension.setRippleBackground(imgGoalEdit, context);
        imgGoalEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter != null) {
                    Extension.startEditGoalActivity(goal, true, ((Activity) context));
                }
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
        checkIfGoalOfDay();
    }

    private void checkIfGoalOfDay() {
        imgIsGoalOfDay.setImageDrawable(goal.isGoalOfDay() ?
                        ContextCompat.getDrawable(context, R.drawable.star_on) :
                        ContextCompat.getDrawable(context, R.drawable.star_off)
        );
        progressBar.setVisibility(goal.isGoalOfDay() ? VISIBLE : GONE);
    }

    public void update(Goal goal, GoalListAdapter adapter) {
        this.goal = goal;
        this.adapter = adapter;
        txtGoalName.setText(goal.getName());
        progressBar.setProgress((int) goal.getStepAmount());
        progressBar.setMax((int) goal.getStepGoal());
        checkIfGoalOfDay();
    }
}
