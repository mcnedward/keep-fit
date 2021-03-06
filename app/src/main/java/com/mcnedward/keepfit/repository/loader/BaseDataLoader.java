package com.mcnedward.keepfit.repository.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.mcnedward.keepfit.model.GoalDate;
import com.mcnedward.keepfit.repository.IGoalRepository;
import com.mcnedward.keepfit.repository.IRepository;
import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 2/13/2016.
 */
public abstract class BaseDataLoader<T, E extends List<T>> extends AsyncTaskLoader<E> implements IDataLoader<T> {
    private static final String TAG = "BaseDataLoader";

    protected E mDataList = null;

    public BaseDataLoader(Context context) {
        super(context);
    }

    @Override
    public E loadInBackground() {
        return buildDataList();
    }

    @Override
    public void deliverResult(E dataList) {
        if (isReset()) {
            return;
        }
        mDataList = dataList;
        if (isStarted()) {
            super.deliverResult(mDataList);
        }
    }

    @Override
    protected void onStartLoading() {
        if (mDataList != null)
            deliverResult(mDataList);
        if (takeContentChanged() || mDataList == null || mDataList.size() == 0)
            forceLoad();
    }

    @Override
    public void onCanceled(E dataList) {
        mDataList = null;
    }

    @Override
    protected void onReset() {
        // Ensure the loader is stopped
        mDataList = null;
    }

    protected abstract E buildDataList();

    protected class InsertTask<T> extends DataChangeTask<T, Void, Void> {

        public InsertTask(Loader<?> loader, T entity, IRepository<T> dataSource) {
            super(loader, entity, dataSource);
        }

        @Override
        protected Void doInBackground(T... params) {
            try {
                mDataSource.save(mEntity);
            } catch (EntityAlreadyExistsException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    protected class UpdateTask<T> extends DataChangeTask<T, Void, Void> {

        public UpdateTask(Loader<?> loader, T entity, IRepository<T> dataSource) {
            super(loader, entity, dataSource);
        }

        @Override
        protected Void doInBackground(T... params) {
            try {
                mDataSource.update(mEntity);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    protected class DeleteTask<T> extends DataChangeTask<T, Void, Void> {

        public DeleteTask(Loader<?> loader, T entity, IRepository<T> dataSource) {
            super(loader, entity, dataSource);
        }

        @Override
        protected Void doInBackground(T... params) {
            try {
                mDataSource.delete(mEntity);
            } catch (EntityDoesNotExistException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
