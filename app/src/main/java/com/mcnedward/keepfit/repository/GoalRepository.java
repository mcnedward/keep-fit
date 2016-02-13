package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.utils.DatabaseHelper;
import com.mcnedward.keepfit.utils.Extension;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 2/8/2016.
 */
public class GoalRepository extends Repository<Goal> {

    public GoalRepository(Context context) {
        super(context);
    }

    @Override
    public Goal save(Goal goal) throws EntityAlreadyExistsException {
        String timestamp = Extension.getTimestamp();
        goal.setCreatedOn(timestamp);
        goal.setUpdatedOn(timestamp);
        // Check for a current goal of day
        List<Goal> goals = read("IsGoalOfDay = 1", null, null, null, null);
        if (!goals.isEmpty())
            goals.get(0).setIsGoalOfDay(false);
        goal.setIsGoalOfDay(true);
        return super.save(goal);
    }

    @Override
    public boolean update(Goal goal) throws EntityDoesNotExistException {
        goal.setUpdatedOn(Extension.getTimestamp());
        return super.update(goal);
    }

    @Override
    public String[] getAllColumns() {
        return new String[] {DatabaseHelper.ID, DatabaseHelper.GOAL, DatabaseHelper.STEP_AMOUNT, DatabaseHelper.STEP_GOAL, DatabaseHelper.IS_GOAL_OF_DAY, DatabaseHelper.CREATED_ON, DatabaseHelper.UPDATED_ON };
    }

    @Override
    public Goal generateObjectFromCursor(Cursor cursor) {
        Goal goal = new Goal();
        goal.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.GOAL)));
        goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.STEP_AMOUNT)));
        goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.STEP_GOAL)));
        goal.setIsGoalOfDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.IS_GOAL_OF_DAY)) == 1);
        goal.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CREATED_ON)));
        goal.setUpdatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.UPDATED_ON)));
        return goal;
    }

    @Override
    public ContentValues generateContentValuesFromEntity(Goal entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.GOAL, entity.getName());
        values.put(DatabaseHelper.STEP_AMOUNT, entity.getStepAmount());
        values.put(DatabaseHelper.STEP_GOAL, entity.getStepGoal());
        values.put(DatabaseHelper.IS_GOAL_OF_DAY, entity.isGoalOfDay());
        values.put(DatabaseHelper.CREATED_ON, entity.getCreatedOn());
        values.put(DatabaseHelper.UPDATED_ON, entity.getUpdatedOn());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.GOAL_TABLE;
    }
}
