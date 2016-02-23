package com.mcnedward.keepfit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Edward on 2/7/2016.
 *
 * Some help taken from: http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    // Database name
    public static String DB_NAME = "Goal.db";
    // Database version - increment this number to upgrade the database
    public static final int DB_VERSION = 10;

    // Tables
    public final static String GOAL_TABLE = "Goals";
    public final static String HISTORY_TABLE = "Histories";
    // Id column, which should be the same across all tables
    public final static String ID = "Id";
    // Goal table
    public final static String G_GOAL = "Name";
    public final static String G_STEP_AMOUNT = "StepAmount";
    public final static String G_STEP_GOAL = "StepGoal";
    public final static String G_IS_GOAL_OF_DAY = "IsGoalOfDay";
    public final static String G_CREATED_ON = "CreatedOn";
    public final static String G_UPDATED_ON = "UpdatedOn";
    // History table
    public final static String H_DATE = "Date";
    public final static String H_GOAL_OF_DAY_ID = "GoalOfDayId";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createGoalTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "%s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s TEXT, %s TEXT)", GOAL_TABLE, ID, G_GOAL, G_STEP_AMOUNT, G_STEP_GOAL, G_IS_GOAL_OF_DAY, G_CREATED_ON, G_UPDATED_ON);
        String createHistoryTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "%s TEXT, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
                HISTORY_TABLE, ID, H_DATE, H_GOAL_OF_DAY_ID, H_GOAL_OF_DAY_ID, G_GOAL, ID);
        sqLiteDatabase.execSQL(createGoalTable);
        sqLiteDatabase.execSQL(createHistoryTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + HISTORY_TABLE);
    }
}
