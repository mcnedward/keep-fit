package com.mcnedward.keepfit.utils.enums;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/8/2016.
 */
public enum Unit implements IBaseEnum {

    METER(1, "Meter", "m") {
        @Override
        public double getConversionMetric(Unit toUnit) {
            double result = 0;
            switch (toUnit) {
                case METER:
                    return 1;
                case KILOMETER:
                    return METER_TO_KILOMETER;
                case MILE:
                    return METER_TO_MILE;
                case YARD:
                    return METER_TO_YARD;
                case STEP:
                    return METER_TO_STEP;
            }
            return result;
        }
    },
    KILOMETER(2, "Kilometer", "km") {
        @Override
        public double getConversionMetric(Unit toUnit) {
            double result = 0;
            switch (toUnit) {
                case METER:
                    return KILOMETER_TO_METER;
                case KILOMETER:
                    return 1;
                case MILE:
                    return KILOMETER_TO_MILE;
                case YARD:
                    return KILOMETER_TO_YARD;
                case STEP:
                    return KILOMETER_TO_STEP;
            }
            return result;
        }
    },
    MILE(3, "Mile", "mi") {
        @Override
        public double getConversionMetric(Unit toUnit) {
            double result = 0;
            switch (toUnit) {
                case METER:
                    return MILE_TO_METER;
                case KILOMETER:
                    return MILE_TO_KILOMETER;
                case MILE:
                    return 1;
                case YARD:
                    return MILE_TO_YARD;
                case STEP:
                    return MILES_TO_STEP;
            }
            return result;
        }
    },
    YARD(4, "Yard", "y") {
        @Override
        public double getConversionMetric(Unit toUnit) {
            double result = 0;
            switch (toUnit) {
                case METER:
                    return YARD_TO_METER;
                case KILOMETER:
                    return YARD_TO_KILOMETER;
                case MILE:
                    return YARD_TO_MILE;
                case YARD:
                    return 1;
                case STEP:
                    return YARD_TO_STEP;
            }
            return result;
        }
    },
    STEP(5, "Step", "s") {
        @Override
        public double getConversionMetric(Unit toUnit) {
            double result = 0;
            switch (toUnit) {
                case METER:
                    return STEP_TO_METER;
                case KILOMETER:
                    return STEP_TO_KILOMETER;
                case MILE:
                    return STEP_TO_MILE;
                case YARD:
                    return STEP_TO_YARD;
                case STEP:
                    return 1;
            }
            return result;
        }
    };

    public int id;
    public String title;
    public String abbreviation;

    Unit(int id, String title, String abbreviation) {
        this.id = id;
        this.title = title;
        this.abbreviation = abbreviation;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public static void setMeterToStep(double range) {
        METER_TO_STEP = range;
    }

    public static List<IBaseEnum> getEnums() {
        List<IBaseEnum> enums = new ArrayList<>();
        enums.add(METER);
        enums.add(KILOMETER);
        enums.add(MILE);
        enums.add(YARD);
        enums.add(STEP);
        return enums;
    }

    public static Unit getById(int id) {
        for (Unit unit : Unit.values())
            if (id == unit.id)
                return unit;
        return null;
    }

    public abstract double getConversionMetric(Unit toUnit);

    public static double convert(Unit fromUnit, Unit toUnit, double amount) {
        if (fromUnit.equals(toUnit))
            return amount;
        double conversionMetric = fromUnit.getConversionMetric(toUnit);
        return amount * conversionMetric;
    }

    public static Unit getByAbbreviation(String abbreviation) {
        switch (abbreviation) {
            case "m":
                return METER;
            case "km":
                return KILOMETER;
            case "mi":
                return MILE;
            case "y":
                return YARD;
            case "s":
                return STEP;
            default:
                return METER;
        }
    }

    /*****
     * Meters To
     *****/

    private static double METER_TO_KILOMETER = 1000;
    private static double METER_TO_MILE = 0.000621371;
    private static double METER_TO_YARD = 1.09361;
    public static double METER_TO_STEP = 1;

    /*****
     * Kilometers To
     ******/

    private static double KILOMETER_TO_METER = 0.001;
    private static double KILOMETER_TO_MILE = 0.621371;
    private static double KILOMETER_TO_YARD = 1093.61;
    private static double KILOMETER_TO_STEP = 762;

    /*****
     * Miles To
     *****/

    private static double MILE_TO_KILOMETER = 1.60934;
    private static double MILE_TO_METER = 1609.34;
    private static double MILE_TO_YARD = 1760;
    private static double MILES_TO_STEP = 2112;

    /*****
     * Yards To
     *****/

    private static double YARD_TO_METER = 0.9144;
    private static double YARD_TO_KILOMETER = 0.0009144;
    private static double YARD_TO_MILE = 0.000568182;
    private static double YARD_TO_STEP = 1.2;

    /*****
     * Steps To
     *****/

    private static double STEP_TO_METER = 1 * METER_TO_STEP;
    private static double STEP_TO_KILOMETER = 1 / KILOMETER_TO_STEP;
    private static double STEP_TO_MILE = 1 / MILES_TO_STEP;
    private static double STEP_TO_YARD = 1 / YARD_TO_STEP;

    public static String format(double number) {
        DecimalFormat format = new DecimalFormat("#.##");
        return format.format(number);
    }
}
