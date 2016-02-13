package com.mcnedward.keepfit.model;

import java.io.Serializable;

/**
 * Created by Edward on 1/31/2016.
 */
public class Goal extends BaseEntity {
    private String name;
    private int stepAmount;
    private int stepGoal;
    private boolean isGoalOfDay;
    private String createdOn;
    private String updatedOn;

    public Goal() {
        super();
        stepAmount = 0;
    }

    public Goal(String name, int stepGoal) {
        this();
        this.name = name;
        this.stepGoal = stepGoal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStepAmount() {
        return stepAmount;
    }

    public void setStepAmount(int stepAmount) {
        this.stepAmount = stepAmount;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public boolean isGoalOfDay() {
        return isGoalOfDay;
    }

    public void setIsGoalOfDay(boolean isGoalOfDay) {
        this.isGoalOfDay = isGoalOfDay;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }
}
