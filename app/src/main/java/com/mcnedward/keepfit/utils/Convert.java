package com.mcnedward.keepfit.utils;

import java.text.DecimalFormat;

/**
 * Created by Edward on 3/8/2016.
 */
public class Convert {

    public static double metersToSteps(double meters) {
        return meters / 0.762;
    }

    public static double stepsToMeters(double steps) {
        return steps * 0.762;
    }

    public static double kilometersToSteps(double kilometers) {
        return kilometers / 0.000762;
    }

    public static double stepsToKilometers(double kilometers) {
        return kilometers * 0.000762;
    }

    public static double yardsToSteps(double yards) {
        return yards / 0.8333333333333334;
    }

    public static double stepsToYards(double yards) {
        return yards * 0.8333333333333334;
    }

    public static double milesToSteps(double miles) {
        return miles / 0.0004734848484848485;
    }

    public static double stepsToMiles(double miles) {
        return miles * 0.0004734848484848485;
    }

    private static String format(double number) {
        DecimalFormat format = new DecimalFormat(".##");
        return format.format(number);
    }

}
