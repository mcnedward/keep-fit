package com.mcnedward.keepfit.thread;

import android.hardware.Sensor;
import android.util.Log;

/**
 * Created by Edward on 3/13/2016.
 */
public abstract class BaseThread extends Thread implements IBaseThread {
    private static final String TAG = "BaseThread";

    private boolean started, running, notifyGoalValuesView;

    public BaseThread() {
        started = false;
        running = false;
    }

    @Override
    public void run() {
        while (running) {
            doRunAction();
        }
    }

    @Override
    public void start() {
        running = true;
        started = true;
        super.start();
    }

    protected abstract void doRunAction();
    protected abstract void doStartAction();
    protected abstract void doStopAction();

    public void startThread() {
        Log.d(TAG, "Starting thread!");
        if (!started)
            start();
        doStartAction();
    }

    public void stopThread() {
        Log.d(TAG, "Stopping thread!");
        doStopAction();
        running = false;
    }
}
