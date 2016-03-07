package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.Extension;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public class GoalDate {

    private String date;
    private Goal goal;

    public GoalDate(String date, Goal goal) {
        this.date = date;
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public Goal getGoal() {
        return goal;
    }
}
