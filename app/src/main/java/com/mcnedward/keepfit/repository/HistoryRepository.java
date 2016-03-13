package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.model.Goal;
import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.utils.Dates;
import com.mcnedward.keepfit.utils.enums.Unit;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Edward on 2/8/2016.
 */
public class HistoryRepository extends Repository<History> implements IHistoryRepository {
    private static final String TAG = "HistoryRepository";

    public HistoryRepository(Context context) {
        super(context);
    }

    public HistoryRepository(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public History getHistoryForCurrentDate() {
        String dateStamp = Dates.getDatabaseDateStamp();
        List<History> histories = read(DatabaseHelper.H_CREATED_ON + " = ?", new String[]{dateStamp}, null, null, null);
        History history;
        if (histories.isEmpty()) {
            history = new History(Dates.getDatabaseDateStamp());
            try {
                history = save(history);
            } catch (EntityAlreadyExistsException e) {
                e.printStackTrace();
            }
        } else {
            history = histories.get(0);
        }
        return history;
    }

    @Override
    public List<Long> getGoalOfDayIds() {
        List<History> histories = readDistinct(null, null, DatabaseHelper.H_CREATED_ON, null, DatabaseHelper.H_CREATED_ON + " ASC", null);
        List<Long> goalOfDayIds = new ArrayList<>();
        for (History h : histories)
            goalOfDayIds.add(h.getGoalOfDayId());
        return goalOfDayIds;
    }

    @Override
    public List<Long> getGoalOfDayIdsInRange(int dateRange) {
        String currentDate = Dates.getDatabaseDateStamp();
        String previousDate = Dates.getDateFromRange(dateRange, Dates.DATABASE_DATE);
        List<History> histories = read(DatabaseHelper.H_CREATED_ON + " BETWEEN ? AND ?",
                new String[]{previousDate, currentDate},
                DatabaseHelper.H_CREATED_ON, null, DatabaseHelper.H_CREATED_ON);
        List<Long> goalOfDayIds = new ArrayList<>();
        for (History h : histories)
            goalOfDayIds.add(h.getGoalOfDayId());
        return goalOfDayIds;
    }

    @Override
    public String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.H_CREATED_ON,
                DatabaseHelper.H_TOTAL_STEPS_FOR_DAY,
                DatabaseHelper.H_GOAL_OF_DAY_ID
        };
    }

    /**
     * Generates a History object from the database cursor.
     *
     * @param cursor The cursor to use for generating the History
     * @return The generated History
     */
    @Override
    public History generateObjectFromCursor(Cursor cursor) {
        History history = new History();
        history.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        history.setCreatedOn(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.H_CREATED_ON)));
        history.setTotalStepsForDay(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.H_TOTAL_STEPS_FOR_DAY)));
        history.setGoalOfDayId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.H_GOAL_OF_DAY_ID)));
        return history;
    }

    /**
     * Generates the values for a History entity to be saved in the database.
     *
     * @param entity The History entity to use for database values
     * @return The ContentValues to use in the database
     */
    @Override
    public ContentValues generateContentValuesFromEntity(History entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.H_CREATED_ON, entity.getCreatedOn());
        values.put(DatabaseHelper.H_TOTAL_STEPS_FOR_DAY, entity.getTotalStepsForDay());
        values.put(DatabaseHelper.H_GOAL_OF_DAY_ID, entity.getGoalOfDayId());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.HISTORY_TABLE;
    }
}
