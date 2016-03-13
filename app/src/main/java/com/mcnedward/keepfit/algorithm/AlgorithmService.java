package com.mcnedward.keepfit.algorithm;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mcnedward.keepfit.listener.AlgorithmListener;

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
        Log.d(TAG, "Creating Algorithm Service!");
        stepDetector = new StepDetector(this);
        edwardAlgorithm = new EdwardAlgorithm();
        stepDetector.registerAlgorithm(edwardAlgorithm);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        thread = new AlgorithmThread();
        listeners = new ArrayList<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Starting Algorithm Service!");
        thread.startAlgorithm();
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

        private boolean started, running;

        public AlgorithmThread() {
            started = false;
            running = false;
        }

        @Override
        public void run() {
            while (running) {
                // Start the algorithm
            }
        }

        @Override
        public void start() {
            running = true;
            started = true;
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), RATE);
            sensorManager.registerListener(stepDetector, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY), RATE);
            super.start();
        }

        public void startAlgorithm() {
            if (!started)
                start();
        }

        public void stopAlgorithm() {
            sensorManager.unregisterListener(stepDetector);
            sensorManager.flush(stepDetector);
            running = false;
        }

    }
}
