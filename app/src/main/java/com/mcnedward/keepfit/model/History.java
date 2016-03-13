package com.mcnedward.keepfit.model;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public class History extends BaseEntity {

    private String createdOn;
    private double totalStepsForDay;
    private long goalOfDayId;

    public History() {

    }

    public History(String createdOn) {
        this.createdOn = createdOn;
    }

    public History(String createdOn, int goalOfDayId) {
        this(createdOn);
        this.goalOfDayId = goalOfDayId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public double getTotalStepsForDay() {
        return totalStepsForDay;
    }

    public void setTotalStepsForDay(double totalStepsForDay) {
        this.totalStepsForDay = totalStepsForDay;
    }

    public long getGoalOfDayId() {
        return goalOfDayId;
    }

    public void setGoalOfDayId(long goalOfDayId) {
        this.goalOfDayId = goalOfDayId;
    }
}
