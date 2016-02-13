package com.mcnedward.keepfit.repository.loader;

import android.content.Context;

import com.mcnedward.keepfit.repository.GoalRepository;
import com.mcnedward.keepfit.repository.IRepository;
import com.mcnedward.keepfit.model.Goal;

import java.util.List;

/**
 * Created by Edward on 1/31/2016.
 */
public class GoalDataLoader extends BaseDataLoader<Goal, List<Goal>> {
    private final static String TAG = "GoalDataLoader";

    private IRepository<Goal> dataSource;

    public GoalDataLoader(Context context) {
        super(context);
        this.dataSource = new GoalRepository(context);
    }

    @Override
    protected List<Goal> buildDataList() {
        return dataSource.retrieve();
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
