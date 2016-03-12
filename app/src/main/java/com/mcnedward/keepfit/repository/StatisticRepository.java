package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Statistic;

import java.util.List;

/**
 * Created by Edward on 2/8/2016.
 */
public class StatisticRepository extends Repository<Statistic> implements IStatisticRepository {
    private static final String TAG = "FragmentCodeRepository";

    public StatisticRepository(Context context) {
        super(context);
    }

    public StatisticRepository(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.S_STAT_ID,
                DatabaseHelper.S_TITLE,
                DatabaseHelper.S_SHOW,
                DatabaseHelper.S_OPEN
        };
    }

    /**
     * Generates a Statistic object from the database cursor.
     *
     * @param cursor The cursor to use for generating the Statistic
     * @return The generated Statistic
     */
    @Override
    public Statistic generateObjectFromCursor(Cursor cursor) {
        Statistic statistic = new Statistic();
        statistic.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        statistic.setStatId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.S_STAT_ID)));
        statistic.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.S_TITLE)));
        statistic.setShow(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.S_SHOW)) == 1);
        statistic.setOpen(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.S_OPEN)) == 1);
        return statistic;
    }

    /**
     * Generates the values for a Statistic entity to be saved in the database.
     *
     * @param entity The Statistic entity to use for database values
     * @return The ContentValues to use in the database
     */
    @Override
    public ContentValues generateContentValuesFromEntity(Statistic entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.S_STAT_ID, entity.getStatId());
        values.put(DatabaseHelper.S_TITLE, entity.getTitle());
        values.put(DatabaseHelper.S_SHOW, entity.isShow());
        values.put(DatabaseHelper.S_OPEN, entity.isOpen());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.STATISTICS_TABLE;
    }
}
