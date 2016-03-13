package com.mcnedward.keepfit.algorithm;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.keepfit.listener.AlgorithmListener;
import com.mcnedward.keepfit.utils.enums.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public class AlgorithmService extends Service {
    private static final String TAG = "AlgorithmService";

    protected static final int RATE = 10000;
    private IStepDetector stepDetector;
    private IAlgorithm edwardAlgorithm;
    private SensorManager sensorManager;
    private AlgorithmThread thread;

    private List<AlgorithmListener> listeners;

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate!");
        stepDetector = new StepDetector(this);
        edwardAlgorithm = new EdwardAlgorithm();
        stepDetector.registerAlgorithm(edwardAlgorithm);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        thread = new AlgorithmThread();
        listeners = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand!");

        boolean runAlgorithm = intent.getBooleanExtra(Settings.RUNNING_ALGORITHM.name(), false);
        if (runAlgorithm)
            thread.startAlgorithm();
        else
            thread.stopAlgorithm();

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

        thread.stopAlgorithm();
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

    private void notifyAlgorithmStarted() {
        for (AlgorithmListener listener : listeners)
            listener.notifyAlgorithmStarted();
    }

    private void notifyAlgorithmStopped() {
        for (AlgorithmListener listener : listeners)
            listener.notifyAlgorithmStopped();
    }

    final class AlgorithmThread extends Thread {

        private boolean started, running, calculate;

        public AlgorithmThread() {
            started = false;
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                // Start the algorithm
                if (calculate) {
                    // Calculate the algorithm

                }
            }
        }

        @Override
        public void start() {
            running = true;
            started = true;
            super.start();
        }

        public void startAlgorithm() {
            Log.d(TAG, "Starting Algorithm Service!");
            if (!started)
                start();
            calculate = true;
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), RATE);
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), RATE);
        }

        public void stopAlgorithm() {
            Log.d(TAG, "Stopping Algorithm Service!");
            sensorManager.unregisterListener(stepDetector);
            sensorManager.flush(stepDetector);
            running = false;

        }

    }
}
