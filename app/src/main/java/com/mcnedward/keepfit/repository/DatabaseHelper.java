package com.mcnedward.keepfit.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Statistic;

/**
 * Created by Edward on 2/7/2016.
 * <p>
 * Some help taken from: http://www.androiddesignpatterns.com/2012/05/correctly-managing-your-sqlite-database.html
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static DatabaseHelper sInstance;

    // Database title
    public static String DB_NAME = "Goal.db";
    // Database version - increment this number to upgrade the database
    public static final int DB_VERSION = 41;

    // Tables
    public static final String GOALS_TABLE = "Goals";
    public static final String HISTORY_TABLE = "History";
    public static final String FRAGMENT_CODES_TABLE = "FragmentCodes";
    public static final String STATISTICS_TABLE = "Statistics";
    // Id column, which should be the same across all tables
    public static final String ID = "Id";
    // Goal table
    public static final String G_GOAL = "Name";
    public static final String G_STEP_GOAL = "StepGoal";
    public static final String G_IS_GOAL_OF_DAY = "IsGoalOfDay";
    public static final String G_UNIT = "Unit";
    public static final String G_HISTORY_ID = "HistoryId";
    // History table
    public static final String H_CREATED_ON = "CreatedOn";
    public static final String H_TOTAL_STEPS_FOR_DAY = "TotalSteps";
    public static final String H_GOAL_OF_DAY_ID = "GoalOfDayId";
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

    /*****
     * CREATE STATEMENTS
     */
    private static final String createGoalTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "%s TEXT, %s INTEGER, %s INTEGER, %s INTEGER, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))",
            GOALS_TABLE, ID, G_GOAL, G_STEP_GOAL, G_IS_GOAL_OF_DAY, G_UNIT, G_HISTORY_ID, G_HISTORY_ID, HISTORY_TABLE, ID);
    private static final String createHistoryTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "%s TEXT, %s REAL, %s INTEGER, FOREIGN KEY(%s) REFERENCES %s(%s))", HISTORY_TABLE, ID, H_CREATED_ON, H_TOTAL_STEPS_FOR_DAY, H_GOAL_OF_DAY_ID, H_GOAL_OF_DAY_ID, GOALS_TABLE, ID);
    private static final String createFragmentCodeTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s INTEGER, %s TEXT)", FRAGMENT_CODES_TABLE, ID, F_CODE_ID, F_TITLE);
    private static final String createStatisticsTable = String.format("CREATE TABLE IF NOT EXISTS %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s INTEGER, %s TEXT, %s INTEGER, %s INTEGER)",
            STATISTICS_TABLE, ID, S_STAT_ID, S_TITLE, S_SHOW, S_OPEN);


    private static String getPopulateFragmentCodeTable() {
        String populateFragmentCodeTable = String.format("INSERT INTO %s (%s, %s) VALUES ", FRAGMENT_CODES_TABLE, F_CODE_ID, F_TITLE);
        for (int i = 0; i < FragmentCode.Code.values().length; i++) {
            FragmentCode.Code code = FragmentCode.Code.values()[i];
            populateFragmentCodeTable += String.format("(%s, '%s')", i + 1, code.title());
            if (i != FragmentCode.Code.values().length - 1)
                populateFragmentCodeTable += ", ";
        }
        return populateFragmentCodeTable;
    }

    private static String getPopulateStatisticsTableQuery() {
        String populateStatisticsTable = String.format("INSERT INTO %s (%s, %s, %s, %s) VALUES ", STATISTICS_TABLE, S_STAT_ID, S_TITLE, S_SHOW, S_OPEN);
        for (int i = 0; i < Statistic.Stat.values().length; i++) {
            Statistic.Stat stat = Statistic.Stat.values()[i];
            populateStatisticsTable += String.format("(%s, '%s', 1, 1)", i + 1, stat.title());
            if (i != FragmentCode.Code.values().length - 1)
                populateStatisticsTable += ", ";
        }
        return populateStatisticsTable;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createGoalTable);
        sqLiteDatabase.execSQL(createHistoryTable);
        sqLiteDatabase.execSQL(createFragmentCodeTable);
        sqLiteDatabase.execSQL(getPopulateFragmentCodeTable());
        sqLiteDatabase.execSQL(createStatisticsTable);
        sqLiteDatabase.execSQL(getPopulateStatisticsTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(TAG, "Update database tables...");
        dropTables(sqLiteDatabase);
        onCreate(sqLiteDatabase);
    }


    private static void resetPreferences(SQLiteDatabase database) {
        database.execSQL(DROP_FRAGMENT_CODES_TABLE);
        database.execSQL(DROP_STATISTICS_TABLE);

        database.execSQL(createFragmentCodeTable);
        database.execSQL(getPopulateFragmentCodeTable());
        database.execSQL(createStatisticsTable);
        database.execSQL(getPopulateStatisticsTableQuery());
    }

    private static void resetData(SQLiteDatabase database) {
        database.execSQL(DROP_GOALS_TABLE);
        database.execSQL(DROP_HISTORY_TABLE);

        database.execSQL(createGoalTable);
        database.execSQL(createHistoryTable);
    }

    public static void resetPreferences(Context context) {
        DatabaseHelper helper = getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        resetPreferences(database);
        helper.close();
    }

    public static void resetData(Context context) {
        DatabaseHelper helper = getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        resetData(database);
        helper.close();
    }

    public static void resetDatabase(Context context) {
        DatabaseHelper helper = getInstance(context);
        SQLiteDatabase database = helper.getWritableDatabase();
        resetPreferences(database);
        resetData(database);
        helper.close();
    }

    /*****
     * DROP Statements
     *****/
    private static final String DROP_GOALS_TABLE = "DROP TABLE IF EXISTS " + GOALS_TABLE;
    private static final String DROP_HISTORY_TABLE = "DROP TABLE IF EXISTS " + HISTORY_TABLE;
    private static final String DROP_FRAGMENT_CODES_TABLE = "DROP TABLE IF EXISTS " + FRAGMENT_CODES_TABLE;
    private static final String DROP_STATISTICS_TABLE = "DROP TABLE IF EXISTS " + STATISTICS_TABLE;

    public void dropTables(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DROP_GOALS_TABLE);
        sqLiteDatabase.execSQL(DROP_HISTORY_TABLE);
        sqLiteDatabase.execSQL(DROP_FRAGMENT_CODES_TABLE);
        sqLiteDatabase.execSQL(DROP_STATISTICS_TABLE);
    }
}
