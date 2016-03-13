package com.mcnedward.keepfit.algorithm;

import android.hardware.SensorEventListener;

import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public interface IStepDetector extends SensorEventListener {
    void addAccelerationData(AccelerationData data);
    void addAccelerationData(List<AccelerationData> data);
    void registerAlgorithm(IAlgorithm algorithm);
    void unregisterAlgorithm(IAlgorithm algorithm);
    List<IAlgorithm> getAlgorithms();
    void reset();
}
