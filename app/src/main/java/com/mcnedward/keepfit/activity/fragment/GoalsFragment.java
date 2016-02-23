package com.mcnedward.keepfit.activity.fragment;

import android.widget.Toast;

import com.mcnedward.keepfit.R;
import com.mcnedward.keepfit.model.Goal;

/**
 * Created by Edward on 2/22/2016.
 */
public class GoalsFragment extends BaseGoalListFragment {

    public static GoalsFragment newInstance(int sectionNumber) {
        return new GoalsFragment();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_goals;
    }

    @Override
    protected void itemClickAction(Goal goal) {
        Toast.makeText(getContext(), "Long tap to set this as the active goal!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void itemLongClickAction(Goal goal) {

    }

}
