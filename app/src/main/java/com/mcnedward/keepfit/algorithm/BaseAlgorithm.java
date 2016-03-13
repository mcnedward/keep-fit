package com.mcnedward.keepfit.algorithm;

import android.content.Context;

import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public abstract class BaseAlgorithm implements IAlgorithm {
    private final static String TAG = "BaseAlgorithm";
    private static final char CSV_DELIM = ',';
    private static final int MILLISEC_FACTOR = 1000000;

    private long startTime;
    private boolean runAlgorithm;   // Boolean to determine whether the algorithm should be run, or if only data should be gathered.
    private String name;
    private AccelerationData accelerationData;


    public BaseAlgorithm(String name) {
        this.name = name;
        startTime = System.currentTimeMillis();
        runAlgorithm = true;
    }

    @Override
    public void notifySensorDataReceived(AccelerationData ad) {
        accelerationData = ad;
        double acceleration = ad.getAcceleration();
        if (runAlgorithm)
            handleSensorData(ad);
    }

    @Override
    public void notifySensorDataReceived(List<AccelerationData> adList) {
        for (AccelerationData ad : adList) {
            notifySensorDataReceived(ad);
        }
    }

    protected abstract void handleSensorData(AccelerationData ad);

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void shouldRunAlgorithm(boolean runAlgorithm) {
        this.runAlgorithm = runAlgorithm;
    }

    @Override
    public AccelerationData getAccelerationData() {
        return accelerationData;
    }
}
