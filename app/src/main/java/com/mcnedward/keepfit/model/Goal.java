package com.mcnedward.keepfit.model;

import java.io.Serializable;

/**
 * Created by Edward on 1/31/2016.
 */
public class Goal implements Serializable {
    private static int goalId = 0;

    private int id;
    private String name;
    private int stepAmount;
    private int stepGoal;

    public Goal() {
        id = goalId++;
        stepAmount = 0;
    }

    public Goal(String name, int stepGoal) {
        this();
        this.name = name;
        this.stepGoal = stepGoal;
    }

    public void setId(int id) { this.id = id; };

    public int getId() {
        return id;
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
}
