package com.mcnedward.keepfit.utils.enums;

/**
 * Created by Edward on 3/8/2016.
 */
public enum Unit {

    METERS(1),
    YARDS(2),
    MILES(3),
    KILOMETERS(4);

    public int id;

    Unit(int id) {
        this.id = id;
    }

    public static Unit getById(int id) {
        for (Unit unit : Unit.values())
            if (id == unit.id)
                return unit;
        return null;
    }

}
