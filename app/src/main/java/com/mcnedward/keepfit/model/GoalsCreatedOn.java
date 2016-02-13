package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.Comparator.GoalNameComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class GoalsCreatedOn {

    private String createdOn;
    private List<Goal> goals;

    public GoalsCreatedOn(String createdOn) {
        this.createdOn = createdOn;
        goals = new ArrayList<>();
    }

    public void add(Goal goal) {
        goals.add(goal);
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
