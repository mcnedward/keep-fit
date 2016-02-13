package com.mcnedward.keepfit.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.GoalListAdapter;
import com.mcnedward.keepfit.utils.KeepFitDatabase;
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
    private ImageView imgGoalEdit;
    private ImageView imgGoalDelete;

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
        inflate(context, R.layout.goal_list_item, this);

        txtGoalName = (TextView) findViewById(R.id.list_goal_name);
        if (goal != null)
            txtGoalName.setText(goal.getName());

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
        imgGoalDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (adapter != null) {
                    try {
                        repository.delete(goal);
                    } catch (EntityDoesNotExistException e) {
                        e.printStackTrace();
                    }
                    adapter.deleteGoal(goal);
                    Toast.makeText(context, "Deleted " + goal.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void update(Goal goal, GoalListAdapter adapter) {
        this.goal = goal;
        this.adapter = adapter;
        txtGoalName.setText(goal.getName());
    }
}
