package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 1/31/2016.
 */
public class Goal extends BaseEntity {

    private String name;
    private double stepAmount;
    private double stepGoal;
    private boolean isGoalOfDay;
    private String createdOn;
    private Unit unit;

    public Goal() {
        super();
        stepAmount = 0;
    }

    public Goal(String name, int stepGoal) {
        this();
        this.name = name;
        this.stepGoal = stepGoal;
    }

    public Goal(String name, int stepGoal, Unit unit) {
        this(name, stepGoal);
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStepAmount() {
        return stepAmount;
    }

    public void setStepAmount(double stepAmount) {
        this.stepAmount = stepAmount;
        if (this.stepAmount > stepGoal)
            this.stepAmount = stepGoal;
        if (this.stepAmount < 0)
            this.stepAmount = 0;
    }

    public double getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(double stepGoal) {
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

    public int getUnitId() {
        return unit.id;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public void setUnit(int unitId) {
        this.unit = Unit.getById(unitId);
    }

    @Override
    public String toString() {
        return String.format("%s - %s / %s", name, stepAmount, stepGoal);
    }
}
