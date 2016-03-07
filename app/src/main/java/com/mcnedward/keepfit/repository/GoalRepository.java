package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.Extension;
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
        return goal;
    }

    public Goal getGoalByName(String goalName) {
        return read(DatabaseHelper.G_GOAL + " = ?", new String[]{goalName}, null, null, null).get(0);
    }

    public Goal getGoalOfDay() {
        String dateStamp = Extension.getDatabaseDateStamp();
        return getGoalOfDayWithDateStamp(dateStamp);
    }

    public Goal getGoalOfDay(String date) {
        String dateStamp = Extension.getDatabaseDateFromPrettyDate(date);
        return getGoalOfDayWithDateStamp(dateStamp);
    }

    private Goal getGoalOfDayWithDateStamp(String dateStamp) {
        List<Goal> goals = read(DatabaseHelper.G_CREATED_ON + " = ?", new String[]{dateStamp}, null, null, null);
        if (!goals.isEmpty())
            for (Goal goal : goals)
                if (goal.isGoalOfDay())
                    return goal;
        return null;
    }

    public void setGoalOfDay(Goal goal) {
        Goal currentGoal = getGoalOfDay();
        if (currentGoal.getStepAmount() > goal.getStepAmount())
            goal.setStepAmount(currentGoal.getStepAmount());
        updateGoalOfDay(currentGoal, false);
        updateGoalOfDay(goal, true);
    }

    @Override
    public List<Goal> getGoalHistory() {
        return readDistinct(DatabaseHelper.G_IS_GOAL_OF_DAY + " = 1", null, DatabaseHelper.G_CREATED_ON, null, DatabaseHelper.G_CREATED_ON, null);
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
        timestamps.add("12-02-2016-07-46-31");
        timestamps.add("11-02-2016-07-46-31");
        timestamps.add("10-02-2016-07-46-31");
        timestamps.add("09-02-2016-07-46-31");
        timestamps.add("08-02-2016-07-46-31");
        Random rand = new Random();
        for (String timestamp : timestamps)
            for (int i = 0; i < 10; i++) {
                int stepGoal = rand.nextInt(1000);
                int stepAmount = rand.nextInt(stepGoal);
                Goal goal = new Goal();
                goal.setName("Goal " + i);
                goal.setStepAmount(stepAmount);
                goal.setStepGoal(stepGoal);
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
                DatabaseHelper.G_CREATED_ON};
    }

    /**
     * Generates a Goal object from the database cursor.
     * It is important to place the StepGoal BEFORE the StepAmount, since calculations done in setStepAmount() are reliant on StepGoal being already set.
     * @param cursor The cursor to use for generating the Goal
     * @return The generated Goal
     */
    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_GOAL)));
        goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_GOAL)));
        goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_AMOUNT)));
        goal.setIsGoalOfDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_IS_GOAL_OF_DAY)) == 1);
        goal.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_CREATED_ON)));
        return goal;
    }

    /**
     * Generates the values for a Goal entity to be saved in the database.
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
        values.put(DatabaseHelper.G_CREATED_ON, entity.getCreatedOn());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOAL_TABLE;
    }
}
