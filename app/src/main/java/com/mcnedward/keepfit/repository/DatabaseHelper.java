package com.mcnedward.keepfit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Statistic;

/**
 * Created by Edward on 2/7/2016.
 * <p/>
 * Some help taken from: http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    // Database title
    public static String DB_NAME = "Goal.db";
    // Database version - increment this number to upgrade the database
    public static final int DB_VERSION = 33;

    // Tables
    public static final String GOALS_TABLE = "Goals";
    public static final String FRAGMENT_CODES_TABLE = "FragmentCodes";
    public static final String STATISTICS_TABLE = "Statistics";
    // Id column, which should be the same across all tables
    public static final String ID = "Id";
    // Goal table
    public static final String G_GOAL = "Name";
    public static final String G_STEP_AMOUNT = "StepAmount";
    public static final String G_STEP_GOAL = "StepGoal";
    public static final String G_IS_GOAL_OF_DAY = "IsGoalOfDay";
    public static final String G_UNIT = "Unit";
    public static final String G_CREATED_ON = "CreatedOn";
    // Fragment Code table
    public static final String F_CODE_ID = "CodeId";
    public static final String F_TITLE = "Title";
    // Statistics table
    public static final String S_STAT_ID = "StatId";
    public static final String S_TITLE = "Title";
    public static final String S_SHOW = "Show";
    public static final String S_OPEN = "Open";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
    }

    // TODO: CREATED_ON AS INTEGER
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createGoalTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "%s TEXT, %s REAL, %s REAL, %s INTEGER, %s INTEGER, %s TEXT)",
                GOALS_TABLE, ID, G_GOAL, G_STEP_AMOUNT, G_STEP_GOAL, G_IS_GOAL_OF_DAY, G_UNIT, G_CREATED_ON);

        String createFragmentCodeTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s INTEGER, %s TEXT)", FRAGMENT_CODES_TABLE, ID, F_CODE_ID, F_TITLE);
        String populateFragmentCodeTable = String.format("INSERT INTO %s (%s, %s) VALUES ", FRAGMENT_CODES_TABLE, F_CODE_ID, F_TITLE);
        for (int i = 0; i < FragmentCode.Code.values().length; i++) {
            FragmentCode.Code code = FragmentCode.Code.values()[i];
            populateFragmentCodeTable += String.format("(%s, '%s')", i + 1, code.title());
            if (i != FragmentCode.Code.values().length - 1)
                populateFragmentCodeTable += ", ";
        }

        String createStatisticsTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER)",
                STATISTICS_TABLE, ID, S_STAT_ID, S_TITLE, S_SHOW, S_OPEN);
        String populateStatisticsTable = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ", STATISTICS_TABLE, S_STAT_ID, S_TITLE, S_SHOW, S_OPEN);
        for (int i = 0; i < Statistic.Stat.values().length; i++) {
            Statistic.Stat stat = Statistic.Stat.values()[i];
            populateStatisticsTable += String.format("(%s, '%s', 1, 1)", i + 1, stat.title());
            if (i != FragmentCode.Code.values().length - 1)
                populateStatisticsTable += ", ";
        }

        sqLiteDatabase.execSQL(createGoalTable);
        sqLiteDatabase.execSQL(createFragmentCodeTable);
        sqLiteDatabase.execSQL(populateFragmentCodeTable);
        sqLiteDatabase.execSQL(createStatisticsTable);
        sqLiteDatabase.execSQL(populateStatisticsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GOALS_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FRAGMENT_CODES_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + STATISTICS_TABLE);
    }
}
