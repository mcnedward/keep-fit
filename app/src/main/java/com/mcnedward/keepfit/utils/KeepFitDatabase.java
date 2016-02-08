package com.mcnedward.keepfit.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mcnedward.keepfit.model.Goal;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by Edward on 2/8/2016.
 */
public class KeepFitDatabase {
    private final static String TAG = "KeepFitDatabse";

    private DatabaseHelper helper;
    private SQLiteDatabase database;

    public KeepFitDatabase(Context context) {
        helper = new DatabaseHelper(context);
        open();
    }

    public SQLiteDatabase openToRead() throws android.database.SQLException {
        database = helper.getReadableDatabase();
        return database;
    }

    public SQLiteDatabase open() throws android.database.SQLException {
        database = helper.getWritableDatabase();
        return database;
    }

    public void close() {
        if (helper != null)
            helper.close();
    }

    public long insert(Goal goal) {
        long id = 0;
        open();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(helper.GOAL, goal.getName());
            values.put(helper.STEP_AMOUNT, goal.getStepAmount());
            values.put(helper.STEP_GOAL, goal.getStepGoal());

            id = database.insert(helper.GOAL_TABLE, null, values);
            database.setTransactionSuccessful();
        } catch (Exception e) {

        } finally {
            database.endTransaction();
            database.close();
        }
        return id;
    }

    public void delete(Goal goal) {
        open();
        database.beginTransaction();
        try {
            database.delete(helper.GOAL_TABLE, helper.GOAL_ID + " = ?",
                    new String[]{String.valueOf(goal.getId())});
        } finally {
            database.endTransaction();
            close();
        }
    }

    public void update(Goal goal) {
        open();
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(helper.GOAL, goal.getName());
            values.put(helper.STEP_AMOUNT, goal.getStepAmount());
            values.put(helper.STEP_GOAL, goal.getStepGoal());

            database.update(helper.GOAL_TABLE, values, helper.GOAL_ID + " = ?",
                    new String[]{String.valueOf(goal.getId())});
        } finally {
            database.endTransaction();
            close();
        }
    }

    public List<Goal> getAllGoals() {
        List<Goal> goals = new ArrayList<Goal>();
        open();
        database.beginTransaction();
        try {
            Cursor cursor = database.query(helper.GOAL_TABLE, new String[] { helper.GOAL_ID, helper.GOAL, helper.STEP_AMOUNT, helper.STEP_GOAL},
                    null, null, null, null, helper.GOAL);
            while (cursor.moveToNext()) {
                Goal goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(helper.GOAL_ID)));
                goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(helper.GOAL)));
                goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_AMOUNT)));
                goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_GOAL)));
                goals.add(goal);
            }
        } finally {
            database.endTransaction();
            close();
        }
        return goals;
    }

    public Goal getGoal(int id) {
        open();
        database.beginTransaction();
        Goal goal = null;
        try {
            Cursor cursor = database.query(helper.GOAL_TABLE, new String[] { helper.GOAL_ID, helper.GOAL, helper.STEP_AMOUNT, helper.STEP_GOAL},
                    helper.GOAL_ID + " = ?", new String[] { String.valueOf(id)}, null, null, null);
            if (cursor.moveToNext()) {
                goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(helper.GOAL_ID)));
                goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(helper.GOAL)));
                goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_AMOUNT)));
                goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_GOAL)));
            }
        } finally {
            database.endTransaction();
            close();
        }
        return goal;
    }

    public Goal getGoal(String goalName) {
        open();
        database.beginTransaction();
        Goal goal = null;
        try {
            Cursor cursor = database.query(helper.GOAL_TABLE, new String[] { helper.GOAL_ID, helper.GOAL, helper.STEP_AMOUNT, helper.STEP_GOAL},
                    helper.GOAL + " LIKE ?", new String[] { goalName}, null, null, null);
            if (cursor.moveToNext()) {
                goal = new Goal();
                goal.setId(cursor.getInt(cursor.getColumnIndexOrThrow(helper.GOAL_ID)));
                goal.setName(cursor.getString(cursor.getColumnIndexOrThrow(helper.GOAL)));
                goal.setStepAmount(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_AMOUNT)));
                goal.setStepGoal(cursor.getInt(cursor.getColumnIndexOrThrow(helper.STEP_GOAL)));
            }
        } finally {
            database.endTransaction();
            close();
        }
        return goal;
    }
}
