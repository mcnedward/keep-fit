package com.mcnedward.keepfit.model;

import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;

/**
 * Created by Edward on 1/31/2016.
 */
public class Goal extends BaseEntity {

    private String name;
    private double stepAmount;
    private double stepGoal;
    private boolean isGoalOfDay;
    private Unit unit;
    private String createdOn;
    private long historyId;

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
        // Ensure that the step amount is always at least equal to the total steps for the day
        if (this.stepAmount > stepGoal)
            this.stepAmount = stepGoal;
        if (this.stepAmount < 0)
            this.stepAmount = 0;
    }

    public boolean isGoalReached() {
        boolean goalReached = stepAmount == stepGoal;
        if (goalReached && !switchColors)
            switchColors = true;
        return goalReached;
    }

    // This is to ensure the progress bar color is only changed when the goal has just been reached, or the goal was at the reached status, but then some steps were removed.
    private boolean switchColors;

    public boolean switchColorsBack() {
        return switchColors;
    }

    public void setSwitchColors(boolean switchColors) {
        this.switchColors = switchColors;
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

    public String getCreatedOn() {
        return createdOn;
    }

    public int getCreatedOnDateNumber() {
        return Dates.getDateAsNumber(createdOn);
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(long historyId) {
        this.historyId = historyId;
    }

    @Override
    public String toString() {
        return String.format("%s - %s / %s", name, stepAmount, stepGoal);
    }
}
