package com.mcnedward.keepfit.repository.loader;

import android.content.Context;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;

import java.util.List;

/**
 * Created by Edward on 3/7/2016.
 */
public class GoalDateDataLoader extends BaseDataLoader<Goal, List<Goal>> {
    private static final String TAG = "GoalDateDataLoader";

    private IGoalRepository dataSource;
    private boolean useRange = false;
    private int range;

    public GoalDateDataLoader(Context context) {
        super(context);
        this.dataSource = new GoalRepository(context);
    }

    @Override
    protected List<Goal> buildDataList() {
        if (useRange) {
            List<Goal> goals = dataSource.getGoalsInRange(range);
            useRange = false;
            return goals;
        } else {
            return dataSource.getGoalHistory();
        }
    }

    public void loadFromRange(int range) {
        this.range = range;
        useRange = true;
        forceLoad();
    }

    public void insert(Goal goal) {
        new InsertTask<>(this, goal, dataSource).execute();
    }

    public void delete(Goal goal) {
        new DeleteTask<>(this, goal, dataSource).execute();
    }

    public void update(Goal goal) {
        new UpdateTask<>(this, goal, dataSource).execute();
    }

}
