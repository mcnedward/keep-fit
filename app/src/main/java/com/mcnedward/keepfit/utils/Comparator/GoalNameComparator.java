package com.mcnedward.keepfit.utils.Comparator;

import com.mcnedward.keepfit.model.Goal;

import java.util.Comparator;

/**
 * Created by Edward on 2/13/2016.
 */
public class GoalNameComparator implements Comparator<Goal> {
    @Override
    public int compare(Goal lhs, Goal rhs) {
        return lhs.getName().compareTo(rhs.getName());
    }
}
