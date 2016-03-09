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
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    // Database title
    public static String DB_NAME = "Goal.db";
    // Database version - increment this number to upgrade the database
    public static final int DB_VERSION = 17;

    // Tables
    public static final String GOAL_TABLE = "Goals";
    // Id column, which should be the same across all tables
    public static final String ID = "Id";
    // Goal table
    public static final String G_GOAL = "Name";
    public static final String G_STEP_AMOUNT = "StepAmount";
    public static final String G_STEP_GOAL = "StepGoal";
    public static final String G_IS_GOAL_OF_DAY = "IsGoalOfDay";
    public static final String G_UNIT = "Unit";
    public static final String G_CREATED_ON = "CreatedOn";

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

    // TODO: CREATED_ON AS INTEGER
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createGoalTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "%s TEXT, %s REAL, %s REAL, %s INTEGER, %s INTEGER, %s INTEGER)",
                GOAL_TABLE, ID, G_GOAL, G_STEP_AMOUNT, G_STEP_GOAL, G_IS_GOAL_OF_DAY, G_UNIT, G_CREATED_ON);
        sqLiteDatabase.execSQL(createGoalTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
    }
}
