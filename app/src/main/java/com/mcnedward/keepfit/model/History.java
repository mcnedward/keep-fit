package com.mcnedward.keepfit.model;

/**
 * Created by Edward on 2/23/2016.
 */
public class History extends BaseEntity {

    private String date;
    private Goal goal;

    public History() {

    }

    public History(String date, Goal goal) {
        this.date = date;
        this.goal = goal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}
