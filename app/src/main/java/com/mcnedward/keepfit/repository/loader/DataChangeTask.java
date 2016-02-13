package com.mcnedward.keepfit.repository.loader;

import android.os.AsyncTask;
import android.support.v4.content.Loader;

import com.mcnedward.keepfit.repository.IRepository;

/**
 * Created by Edward on 2/13/2016.
 */
public abstract class DataChangeTask<T1, T2, T3> extends AsyncTask<T1, T2, T3> {

    private Loader<?> mLoader;
    protected T1 mEntity;
    protected IRepository<T1> mDataSource;

    public DataChangeTask(Loader<?> loader, T1 entity, IRepository<T1> dataSource) {
        mLoader = loader;
        mEntity = entity;
        mDataSource = dataSource;
    }

    @Override
    protected void onPostExecute(T3 param) {
        mLoader.onContentChanged();
    }
}
