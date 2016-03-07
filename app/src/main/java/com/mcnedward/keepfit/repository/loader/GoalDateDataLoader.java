package com.mcnedward.keepfit.repository.loader;

import android.content.Context;

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

    public GoalDateDataLoader(Context context) {
        super(context);
        this.dataSource = new GoalRepository(context);
    }

    @Override
    protected List<Goal> buildDataList() {
        return dataSource.getGoalHistory();
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
