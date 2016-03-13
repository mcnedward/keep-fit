package com.mcnedward.keepfit.algorithm;

import java.util.List;

/**
 * Created by Edward on 3/13/2016.
 */
public interface IAlgorithm {
    void notifySensorDataReceived(AccelerationData ad);

    void notifySensorDataReceived(List<AccelerationData> adList);

    void shouldRunAlgorithm(boolean runAlgorithm);

    int getStepCount();

    String getName();

    AccelerationData getAccelerationData();
}
