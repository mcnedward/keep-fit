package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.GoalDate;
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
        String datestamp = Extension.getDateStamp();
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
        String datestamp = Extension.getDateStamp();
        List<Goal> goals = read(DatabaseHelper.G_CREATED_ON + " = ?", new String[]{datestamp}, null, null, null);
        if (!goals.isEmpty())
            for (Goal goal : goals)
                if (goal.isGoalOfDay())
                    return goal;
        return null;
    }

    public void setGoalOfDay(Goal goal) {
        Goal currentGoal = getGoalOfDay();
        updateGoalOfDay(currentGoal, false);
        updateGoalOfDay(goal, true);
    }

    @Override
    public List<GoalDate> getGoalDates() {
        List<GoalDate> goalDates = new ArrayList<>();
        List<Goal> dates = readDistinct("", null, DatabaseHelper.G_CREATED_ON, null, DatabaseHelper.G_CREATED_ON, null);
        for (Goal goal : dates) {
            goalDates.add(new GoalDate(goal.getCreatedOn()));
        }
        List<Goal> goals = retrieve();
        for (Goal goal : goals) {
            for (GoalDate date : goalDates) {
                if (date.getDate().equals(goal.getCreatedOn())) {
                    date.add(goal);
                    continue;
                }
            }
        }
        return goalDates;
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

//    public List<GoalDates> getGoalDates() {
//        List<GoalDates> goalsCreatedOn = new ArrayList<>();
//        // Get the distinct goal dates
//        List<Goal> goalDates = readDistinct(null, null, DatabaseHelper.G_CREATED_ON, null, null, null);
//        List<String> dates = new ArrayList<>();
//        for (Goal goal : goalDates)
//            dates.add(goal.getDate());
//        // Sort all of the goals by date
//        List<Goal> goals = retrieve();
//        for (String date : dates) {
//            GoalDates gco = new GoalDates(date);
//            for (Goal goal : goals) {
//                if (goal.getDate().equals(date)) {
//                    gco.add(goal);
//                }
//            }
//            goalsCreatedOn.add(gco);
//        }
//        return goalsCreatedOn;
//    }

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
        return new String[]{DatabaseHelper.ID, DatabaseHelper.G_GOAL, DatabaseHelper.G_STEP_AMOUNT, DatabaseHelper.G_STEP_GOAL, DatabaseHelper.G_IS_GOAL_OF_DAY, DatabaseHelper.G_CREATED_ON};
    }

    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_GOAL)));
        goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_AMOUNT)));
        goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_STEP_GOAL)));
        goal.setIsGoalOfDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.G_IS_GOAL_OF_DAY)) == 1);
        goal.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.G_CREATED_ON)));
        return goal;
    }

    @Override
    public ContentValues generateContentValuesFromEntity(Goal entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.G_GOAL, entity.getName());
        values.put(DatabaseHelper.G_STEP_AMOUNT, entity.getStepAmount());
        values.put(DatabaseHelper.G_STEP_GOAL, entity.getStepGoal());
        values.put(DatabaseHelper.G_IS_GOAL_OF_DAY, entity.isGoalOfDay());
        values.put(DatabaseHelper.G_CREATED_ON, entity.getCreatedOn());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOAL_TABLE;
    }
}
