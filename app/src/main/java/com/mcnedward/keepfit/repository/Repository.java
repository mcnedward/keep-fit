package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mcnedward.keepfit.model.BaseEntity;
import com.mcnedward.keepfit.utils.DatabaseHelper;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/8/2016.
 */
public abstract class Repository<T extends BaseEntity> implements IRepository<T> {
    private final static String TAG = "Repository";

    private final static String WHERE_ID_CLAUSE = "Id = ?";
    private DatabaseHelper helper;
    protected SQLiteDatabase database;

    public Repository(Context context) {
        helper = new DatabaseHelper(context);
        open();
    }

    public T get(int id) {
        List<T> dataList = read(WHERE_ID_CLAUSE, new String[]{String.valueOf(id)}, null, null, null);
        return dataList.get(0);
    }

    public T get(String... args) {
        List<String> selectionArgs = new ArrayList<>();
        for (String arg : args)
            selectionArgs.add(arg);
        List<T> dataList = read(WHERE_ID_CLAUSE, selectionArgs.toArray(new String[selectionArgs.size()]), null, null, null);
        return dataList.get(0);
    }

    /**
     * Save an entity in the database.
     *
     * @param entity The entity to save to the database.
     * @return True if the entity was saved, false otherwise.
     * @throws EntityAlreadyExistsException If the entity already exists in the database.
     */
    public T save(T entity) throws EntityAlreadyExistsException {
        if (entity == null && entityExists(entity.getId()))
            throw new EntityAlreadyExistsException(entity.getId());
        return insert(entity);
    }

    /**
     * Update an existing entity.
     *
     * @param entity The entity to update.
     * @return True if the entity was updated, false otherwise
     * @throws EntityDoesNotExistException If the entity does not exist.
     */
    public boolean update(T entity) throws EntityDoesNotExistException {
        if (!entityExists(entity.getId()))
            throw new EntityDoesNotExistException(entity.getId());
        return change(entity);
    }

    /**
     * Delete an existing entity.
     *
     * @param entity The entity to delete.
     * @return True if the entity was deleted, false otherwise.
     * @throws EntityDoesNotExistException If the entity does not exist.
     */
    public boolean delete(T entity) throws EntityDoesNotExistException {
        if (!entityExists(entity.getId()))
            throw new EntityDoesNotExistException(entity.getId());
        return remove(entity);
    }

    /**
     * Attempts to insert an entity into the database. This does not check if an
     * entity already exists.
     *
     * @param entity The entity to insert into the database.
     * @return True if the entity was inserted, false otherwise.
     */
    private final T insert(T entity) {
        try {
            database.beginTransaction();
            long id = database.insert(getTableName(), null,
                    generateContentValuesFromEntity(entity));
            entity.setId(id);
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error when trying to insert " + entity, e);
        } finally {
            database.endTransaction();
        }
        return entity;
    }

    private final boolean change(T entity) {
        int rowsUpdated = 0;
        try {
            database.beginTransaction();
            ContentValues values = generateContentValuesFromEntity(entity);
            rowsUpdated = database.update(getTableName(), values, WHERE_ID_CLAUSE, new String[]{String.valueOf(entity.getId())});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error when trying to insert " + entity, e);
        } finally {
            database.endTransaction();
        }
        return rowsUpdated != 0;
    }

    /**
     * Delete an entity from the database.
     *
     * @param entity The entity to delete.
     * @return True if the entity was deleted, false otherwise.
     */
    private final boolean remove(T entity) {
        int rowsDeleted = 0;
        try {
            database.beginTransaction();
            rowsDeleted = database.delete(getTableName(), WHERE_ID_CLAUSE, new String[]{String.valueOf(entity.getId())});
            database.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error when trying to insert " + entity, e);
        } finally {
            database.endTransaction();
        }
        return rowsDeleted != 0;
    }

    /**
     * Retrieve all data for this source.
     *
     * @return A list of all entities in this data source.
     */
    public List<T> read() {
        return read(null, null, null, null, null);
    }

    /**
     * Queries the data table based on the options passed in.
     *
     * @param selection     The filter of which rows to return (WHERE clause), with arguments passed in as "?"
     * @param selectionArgs The arguments for the WHERE clause
     * @param groupBy       How to group rows
     * @param having        Which rows to include in the cursor results
     * @param orderBy       The order of the results to return
     * @return A list of data from the table
     */
    public List<T> read(String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        List<T> data = new ArrayList<>();
        Cursor cursor = null;
        database.beginTransaction();
        try {
            cursor = database.query(getTableName(), getAllColumns(), selection, selectionArgs, groupBy, having, orderBy);
            while (cursor.moveToNext()) {
                data.add(generateObjectFromCursor(cursor));
            }
        } finally {
            if (cursor != null)
                cursor.close();
            database.endTransaction();
        }
        return data;
    }

    protected abstract String[] getAllColumns();

    protected abstract T generateObjectFromCursor(Cursor cursor);

    protected abstract ContentValues generateContentValuesFromEntity(T entity);

    protected abstract String getTableName();

    /**
     * This checks if an entity with a certain id already exists in the
     * database.
     *
     * @param id The id of the entity of check.
     * @return True if the entity already exists, false otherwise.
     */
    private boolean entityExists(long id) {
        Cursor cursor = null;
        try {
            cursor = database.query(getTableName(), new String[]{DatabaseHelper.ID},
                    DatabaseHelper.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } catch (Exception e) {
            Log.e(TAG, "Error checking if entity exists with id: " + id, e);
            return false;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    private SQLiteDatabase openToRead() throws android.database.SQLException {
        database = helper.getReadableDatabase();
        return database;
    }

    private SQLiteDatabase open() throws android.database.SQLException {
        database = helper.getWritableDatabase();
        return database;
    }

    private void close() {
        if (helper != null)
            helper.close();
    }
}
