package com.mcnedward.keepfit.model;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public class History extends BaseEntity {

    private String date;
    private List<Goal> goals;

    public History() {

    }

    public History(String date) {
        this.date = date;
    }

    public History(String date, List<Goal> goals) {
        this(date);
        this.goals = goals;
    }

    public History(String date, Goal goal) {
        this(date);
        addGoal(goal);
    }

    public void addGoal(Goal goal) {
        goals.add(goal);
    }

    public Goal getGoalOfDay() {
        for (Goal goal : goals)
            if (goal.isGoalOfDay())
                return goal;
        return null;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Goal> getGoals() {
        return goals;
    }

    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }
}
