package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.mcnedward.keepfit.activity.fragment.BaseFragment;
import com.mcnedward.keepfit.model.FragmentCode;
import com.mcnedward.keepfit.model.Goal;
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
public class FragmentCodeRepository extends Repository<FragmentCode> implements IFragmentCodeRepository {
    private static final String TAG = "FragmentCodeRepository";

    public FragmentCodeRepository(Context context) {
        super(context);
    }

    public FragmentCodeRepository(DatabaseHelper helper) {
        super(helper);
    }

    @Override
    public List<FragmentCode> getFragmentCodesSorted() {
        return retrieve(DatabaseHelper.F_CODE_ID, "" , DatabaseHelper.F_CODE_ID);
    }

    @Override
    public String[] getAllColumns() {
        return new String[]{
                DatabaseHelper.ID,
                DatabaseHelper.F_CODE_ID,
                DatabaseHelper.F_TITLE};
    }

    /**
     * Generates a FragmentCode object from the database cursor.
     *
     * @param cursor The cursor to use for generating the FragmentCode
     * @return The generated FragmentCode
     */
    @Override
    public FragmentCode generateObjectFromCursor(Cursor cursor) {
        FragmentCode code = new FragmentCode();
        code.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.ID)));
        code.setCodeId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.F_CODE_ID)));
        code.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.F_TITLE)));
        return code;
    }

    /**
     * Generates the values for a FragmentCode entity to be saved in the database.
     *
     * @param entity The FragmentCode entity to use for database values
     * @return The ContentValues to use in the database
     */
    @Override
    public ContentValues generateContentValuesFromEntity(FragmentCode entity) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.ID, entity.getId());
        values.put(DatabaseHelper.F_CODE_ID, entity.getCodeId());
        values.put(DatabaseHelper.F_TITLE, entity.getTitle());
        return values;
    }

    @Override
    public String getTableName() {
        return DatabaseHelper.FRAGMENT_CODES_TABLE;
    }
}
