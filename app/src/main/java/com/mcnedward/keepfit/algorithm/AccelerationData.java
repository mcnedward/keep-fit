package com.mcnedward.keepfit.algorithm;

/**
 * Created by Edward on 3/13/2016.
 */
public class AccelerationData {

    private final double x;
    private final double y;
    private final double z;
    private double acceleration;
    private final long timeStamp;

    public AccelerationData(double x, double y, double z, long timeStamp) {
        this(x, y, z, 0, timeStamp);
    }

    public AccelerationData(double x, double y, double z, double acceleration, long timeStamp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.acceleration = acceleration;
        this.timeStamp = timeStamp;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public double getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    @Override
    public String toString() {
        return String.format("X: %s; Y: %s; Z:%s; Acceleration: %s; Time: %s", x, y, z, acceleration, timeStamp);
    }

}
