package com.mcnedward.keepfit.repository.loader;

import android.content.Context;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalDate;
import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IGoalRepository;

import java.util.List;

/**
 * Created by Edward on 3/7/2016.
 */
public class GoalDateDataLoader extends BaseDataLoader<GoalDate, List<GoalDate>> {
    private static final String TAG = "GoalDataLoader";

    private IGoalRepository dataSource;

    public GoalDateDataLoader(Context context) {
        super(context);
        this.dataSource = new GoalRepository(context);
    }

    @Override
    protected List<GoalDate> buildDataList() {
        return dataSource.getGoalDates();
    }

    @Override
    public void insert(GoalDate goalDate) {
        for (Goal goal : goalDate.getGoals())
            new InsertTask<>(this, goal, dataSource).execute();
    }

    @Override
    public void delete(GoalDate goalDate) {
        for (Goal goal : goalDate.getGoals())
            new DeleteTask<>(this, goal, dataSource).execute();
    }

    @Override
    public void update(GoalDate goalDate) {
        for (Goal goal : goalDate.getGoals())
            new UpdateTask<>(this, goal, dataSource).execute();
    }
}
