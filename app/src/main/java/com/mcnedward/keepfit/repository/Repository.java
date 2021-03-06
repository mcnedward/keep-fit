package com.mcnedward.keepfit.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mcnedward.keepfit.model.BaseEntity;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 2/8/2016.
 */
public abstract class Repository<T extends BaseEntity> implements IRepository<T> {
    private static final String TAG = "Repository";

    private static final String WHERE_ID_CLAUSE = "Id = ?";
    private DatabaseHelper helper;
    protected SQLiteDatabase database;

    public Repository(Context context) {
        this(DatabaseHelper.getInstance(context));
        open();
    }

    public Repository(DatabaseHelper helper) {
        this.helper = helper;
        open();
    }

    public T get(long id) {
        List<T> dataList = read(WHERE_ID_CLAUSE, new String[]{String.valueOf(id)}, null, null, null);
        return !dataList.isEmpty() ? dataList.get(0) : null;
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
    @Override
    public List<T> retrieve() {
        return read(null, null, null, null, null);
    }

    /**
     * Retrieve all data for this source.
     * @param groupBy
     * @param having
     * @param orderBy
     * @return A list of all entities in this data source.
     */
    @Override
    public List<T> retrieve(String groupBy, String having, String orderBy) {
        return read(null, null, groupBy, having, orderBy);
    }

    /**
     * Queries the data table based on the options passed in.
     *
     * @param whereClause   The filter of which rows to return (WHERE clause), with arguments passed in as "?"
     * @param whereArgs     The arguments for the WHERE clause
     * @param groupBy       How to group rows
     * @param having        Which rows to include in the cursor results
     * @param orderBy       The order of the results to return
     * @return A list of data from the table
     */
    protected List<T> read(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy) {
        if (!database.isOpen()) return new ArrayList<>();
        List<T> data = new ArrayList<>();
        Cursor cursor = null;
        try {
            database.beginTransaction();
            cursor = database.query(getTableName(), getAllColumns(), whereClause, whereArgs, groupBy, having, orderBy);
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

    protected List<T> readDistinct(String whereClause, String[] whereArgs, String groupBy, String having, String orderBy, String limit) {
        List<T> data = new ArrayList<>();
        Cursor cursor = null;
        try {
            database.beginTransaction();
            cursor = database.query(true, getTableName(), getAllColumns(), whereClause, whereArgs, groupBy, having, orderBy, limit);
            while (cursor.moveToNext()) {
                data.add(generateObjectFromCursor(cursor));
            }
            database.setTransactionSuccessful();
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

    @Override
    public void close() {
        if (helper != null)
            helper.close();
    }
}
