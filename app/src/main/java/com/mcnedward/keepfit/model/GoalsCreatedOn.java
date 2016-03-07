package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.Extension;

import java.util.ArrayList;
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
        // TODO CHANGE THIS!!!!!
        return Extension.getPrettyDate(createdOn);
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
