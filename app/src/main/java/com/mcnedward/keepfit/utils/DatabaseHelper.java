package com.mcnedward.keepfit.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Edward on 2/7/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String TAG = "DatabaseHelper";

    // Database name
    public static String DB_NAME = "Goal.db";
    // Database version - increment this number to updgrade the database
    public static final int DB_VERSION = 1;

    public final static String GOAL_TABLE = "Goals";
    public final static String GOAL_ID = "GoalId";
    public final static String GOAL = "Name";
    public final static String STEP_AMOUNT = "StepAmount";
    public final static String STEP_GOAL = "StepGoal";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createGoalTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "%s TEXT, %s INTEGER, %s INTEGER)", GOAL_TABLE, GOAL_ID, GOAL, STEP_AMOUNT, STEP_GOAL);
        sqLiteDatabase.execSQL(createGoalTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GOAL_TABLE);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {

    }
}
