package com.mcnedward.keepfit.utils.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.mcnedward.keepfit.model.Goal;

import java.util.List;

/**
 * Created by Edward on 1/31/2016.
 */
public class GoalDataLoader extends AsyncTaskLoader<Goal> {
    private final static String TAG = "GoalDataLoader";

    private List<Goal> dataList;

    public GoalDataLoader(Context context) {
        super(context);
    }

    @Override
    public Goal loadInBackground() {
        return null;
    }

}
