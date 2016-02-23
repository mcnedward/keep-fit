package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mcnedward.keepfit.model.History;
import com.mcnedward.keepfit.utils.Extension;

import java.util.List;

/**
 * Created by Edward on 2/23/2016.
 */
public class HistoryRepository extends Repository<History> implements IHistoryRepository {
    private final static String TAG = "HistoryRepository";

    private GoalRepository goalRepository;

    public HistoryRepository(Context context) {
        super(context);
        goalRepository = new GoalRepository(context);
    }

    public HistoryRepository(Context context, GoalRepository goalRepository) {
        super(context);
        this.goalRepository = goalRepository;
    }

    public History getHistoryForCurrentDate() {
        History history = null;
        String currentDate = Extension.getDateStamp();
        List<History> historyList = read(DatabaseHelper.H_DATE + " = ?", new String[]{currentDate}, null, null, null);
        if (!historyList.isEmpty())
           history = historyList.get(0);
        return history;
    }

    public History getHistoryForDate(String date) {
        History history = null;
        List<History> historyList = read(DatabaseHelper.H_DATE + " = ?", new String[]{date}, null, null, null);
        if (!historyList.isEmpty())
            history = historyList.get(0);
        return history;
    }

    @Override
    protected String[] getAllColumns() {
        return new String[]{DatabaseHelper.ID, DatabaseHelper.H_DATE, DatabaseHelper.H_GOAL_OF_DAY_ID};
    }

    @Override
    protected History generateObjectFromCursor(Cursor cursor) {
        History history = new History();
        history.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        history.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.H_DATE)));
        int goalId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.H_GOAL_OF_DAY_ID));
        history.setGoal(goalRepository.get(goalId));
        return history;
    }

    @Override
    protected ContentValues generateContentValuesFromEntity(History entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.H_DATE, entity.getDate());
        values.put(DatabaseHelper.H_GOAL_OF_DAY_ID, entity.getGoal() != null ? entity.getGoal().getId() : null);
        return values;
    }

    @Override
    protected String getTableName() {
        return DatabaseHelper.HISTORY_TABLE;
    }
}
