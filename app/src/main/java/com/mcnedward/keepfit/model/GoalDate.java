package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.Extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class GoalDate {

    private String date;
    private List<Goal> goals;

    public GoalDate(String date) {
        this.date = date;
        goals = new ArrayList<>();
    }

    public void add(Goal goal) {
        goals.add(goal);
    }

    public String getDate() {
        return date;
    }

    public List<Goal> getGoals() {
        return goals;
    }
}
