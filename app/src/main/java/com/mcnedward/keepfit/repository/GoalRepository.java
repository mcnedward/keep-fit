package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.activity.MainActivity;
import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/8/2016.
 */
public class GoalRepository extends Repository<Goal> implements IGoalRepository {
    private static final String TAG = "GoalRepository";

    public GoalRepository(Context context) {
        super(context);
    }

    @Override
    public Goal save(Goal goal) throws EntityAlreadyExistsException {
        String datestamp = Extension.getDatabaseDateStamp();
        goal.setCreatedOn(datestamp);
        goal.setIsGoalOfDay(true);
        Goal currentGoal = getGoalOfDay();
        updateGoalOfDay(currentGoal, false);
        goal = super.save(goal);
        fillOldDateTestData();
        return goal;
    }

    @Override
    public Goal getGoalByName(String goalName) {
        return read(DatabaseHelper.G_GOAL + " = ?", new String[]{goalName}, null, null, null).get(0);
    }

    @Override
    public Goal getGoalOfDay() {
        String dateStamp = Extension.getDatabaseDateStamp();
        List<Goal> goals = read(DatabaseHelper.G_CREATED_ON + " = ?", new String[]{dateStamp}, null, null, null);
        if (!goals.isEmpty())
            for (Goal goal : goals)
                if (goal.isGoalOfDay())
                    return goal;
        return null;
    }

    @Override
    public void setGoalOfDay(Goal goal) {
        Goal currentGoal = getGoalOfDay();
        if (currentGoal != null) {
            if (currentGoal.getStepAmount() > goal.getStepAmount())
                goal.setStepAmount(currentGoal.getStepAmount());
            updateGoalOfDay(currentGoal, false);
        }
        updateGoalOfDay(goal, true);
    }

    @Override
    public List<Goal> getGoalHistory() {
        return readDistinct(DatabaseHelper.G_IS_GOAL_OF_DAY + " = 1", null, DatabaseHelper.G_CREATED_ON, null, DatabaseHelper.G_CREATED_ON, null);
    }

    @Override
    public List<Goal> getGoalsForDay() {
        String dateStamp = Extension.getDatabaseDateStamp();
        List<Goal> goals = read(DatabaseHelper.G_CREATED_ON + " = ?", new String[]{dateStamp}, null, null, null);
        return goals;
    }

    private void updateGoalOfDay(Goal goal, boolean isGoalOfDay) {
        if (goal != null) {
            goal.setIsGoalOfDay(isGoalOfDay);
            try {
                update(goal);
            } catch (EntityDoesNotExistException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public void fillOldDateTestData() {
        List<String> timestamps = new ArrayList<>();
        timestamps.add("20160307");
        timestamps.add("20160306");
        timestamps.add("20160305");
        timestamps.add("20160304");
        timestamps.add("20160303");
        timestamps.add("20160302");
        timestamps.add("20160301");
        timestamps.add("20160229");
        timestamps.add("20160228");
        timestamps.add("20160227");
        timestamps.add("20160226");
        timestamps.add("20160225");
        timestamps.add("20160224");
        timestamps.add("20160223");
        timestamps.add("20160222");
        timestamps.add("20160221");
        timestamps.add("20160220");
        timestamps.add("20160129");
        timestamps.add("20160128");
        timestamps.add("20160127");
        timestamps.add("20160125");
        timestamps.add("20160123");
        timestamps.add("20160122");
        timestamps.add("20160121");
        timestamps.add("20160120");
        timestamps.add("20160119");
        timestamps.add("20160118");
        Random rand = new Random();
        for (String timestamp : timestamps) {
            int stepGoal = rand.nextInt(1000);
            int stepAmount = rand.nextInt(stepGoal / 2);
            Goal goal = new Goal();
            goal.setName("Goal " + stepGoal);
            goal.setStepGoal(stepGoal);
            goal.setStepAmount(stepAmount);
            goal.setIsGoalOfDay(true);
            goal.setUnit(Unit.METER);
            goal.setCreatedOn(timestamp);
            try {
                super.save(goal);
            } catch (EntityAlreadyExistsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.G_GOAL,
                DatabaseHelper.G_STEP_AMOUNT,
                DatabaseHelper.G_STEP_GOAL,
                DatabaseHelper.G_IS_GOAL_OF_DAY,
                DatabaseHelper.G_UNIT,
                DatabaseHelper.G_CREATED_ON};
    }

    /**
     * Generates a Goal object from the database cursor.
     * It is important to place the StepGoal BEFORE the StepAmount, since calculations done in setStepAmount() are reliant on StepGoal being already set.
     *
     * @param cursor The cursor to use for generating the Goal
     * @return The generated Goal
     */
    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_GOAL)));
        goal.setStepGoal(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_GOAL)));
        goal.setStepAmount(cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_AMOUNT)));
        goal.setIsGoalOfDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_IS_GOAL_OF_DAY)) == 1);
        goal.setUnit(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_UNIT)));
        goal.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_CREATED_ON)));
        return goal;
    }

    /**
     * Generates the values for a Goal entity to be saved in the database.
     *
     * @param entity The Goal entity to use for database values
     * @return The ContentValues to use in the database
     */
    @Override
    public ContentValues generateContentValuesFromEntity(Goal entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.G_GOAL, entity.getName());
        values.put(DatabaseHelper.G_STEP_GOAL, entity.getStepGoal());
        values.put(DatabaseHelper.G_STEP_AMOUNT, entity.getStepAmount());
        values.put(DatabaseHelper.G_IS_GOAL_OF_DAY, entity.isGoalOfDay());
        values.put(DatabaseHelper.G_UNIT, entity.getUnitId());
        values.put(DatabaseHelper.G_CREATED_ON, entity.getCreatedOn());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOAL_TABLE;
    }
}
