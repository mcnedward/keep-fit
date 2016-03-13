package com.mcnedward.keepfit.algorithm;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.keepfit.listener.AlgorithmListener;
import com.mcnedward.keepfit.thread.BaseThread;
import com.mcnedward.keepfit.utils.enums.Settings;
import com.mcnedward.keepfit.view.GoalValuesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public class AlgorithmService extends Service {
    private static final String TAG = "AlgorithmService";

    protected static final int RATE = 10000;
    private IStepDetector stepDetector;
    private static IAlgorithm EDWARD_ALGORITHM;
    private SensorManager sensorManager;
    private AlgorithmThread thread;
    private static GoalValuesView GOAL_VALUES_VIEW;

    private List<AlgorithmListener> listeners;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate!");
        stepDetector = new StepDetector(this);
        EDWARD_ALGORITHM = new EdwardAlgorithm();
        stepDetector.registerAlgorithm(EDWARD_ALGORITHM);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        thread = new AlgorithmThread();
        listeners = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand!");

        if (intent != null) {
            boolean runAlgorithm = intent.getBooleanExtra(Settings.RUNNING_ALGORITHM.name(), false);
            if (runAlgorithm)
                thread.startThread();
            else
                thread.pauseThread();
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Destroying Algorithm Service!");

        thread.stopThread();
        sensorManager.flush(stepDetector);
        boolean retry = false;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        thread = null;
        stopSelf();
    }

    public static void setGoalValuesView(GoalValuesView view) {
        if (GOAL_VALUES_VIEW == null)
            GOAL_VALUES_VIEW = view;
    }

    public static IAlgorithm getAlgorithm() {
        return EDWARD_ALGORITHM;
    }

    final class AlgorithmThread extends BaseThread {

        private boolean notifyGoalValuesView;

        @Override
        public void doRunAction() {
            // Start the algorithm
            if (notifyGoalValuesView) {
                // Calculate the algorithm
                notifyGoalValuesView = false;
                GOAL_VALUES_VIEW.notifyAlgorithmRunning(true);
            }
        }

        @Override
        public void doStartAction() {
            Log.d(TAG, "Starting Algorithm Service!");
            notifyGoalValuesView = true;
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), RATE);
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), RATE);
        }

        @Override
        public void pauseThread() {
            notifyGoalValuesView = false;
        }

        @Override
        public void doStopAction() {
            Log.d(TAG, "Stopping Algorithm Service!");
            sensorManager.unregisterListener(stepDetector);
            sensorManager.flush(stepDetector);
        }

    }
}
