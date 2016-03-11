package com.mcnedward.keepfit.repository.loader;

import android.content.Context;

import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.model.Goal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 1/31/2016.
 */
public class GoalDataLoader extends BaseDataLoader<Goal, List<Goal>> {
    private static final String TAG = "GoalDataLoader";

    private IGoalRepository repository;

    public GoalDataLoader(Context context) {
        super(context);
        this.repository = new GoalRepository(context);
    }

    @Override
    protected List<Goal> buildDataList() {
        return repository.getGoalsForDay();
    }

    public void insert(Goal goal) {
        new InsertTask<>(this, goal, repository).execute();
    }

    public void delete(Goal goal) {
        new DeleteTask<>(this, goal, repository).execute();
    }

    public void update(Goal goal) {
        new UpdateTask<>(this, goal, repository).execute();
    }

}
