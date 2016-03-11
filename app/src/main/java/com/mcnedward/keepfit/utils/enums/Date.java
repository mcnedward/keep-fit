package com.mcnedward.keepfit.utils.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Edward on 3/11/2016.
 */
public enum Date implements IBaseEnum {

    WEEK("Week", 7),
    MONTH("Month", 30),
    TWO_MONTHS("2 Months", 60),
    THREE_MONTHS("3 Months", 90),
    FOUR_MONTHS("4 Months", 120);

    public String title;
    public int maxDays;

    Date(String title, int maxDays) {
        this.title = title;
        this.maxDays = maxDays;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public int getMaxDays() {
        return maxDays;
    }

    public static List<IBaseEnum> getEnums() {
        List<IBaseEnum> enums = new ArrayList<>();
        enums.add(WEEK);
        enums.add(MONTH);
        enums.add(TWO_MONTHS);
        enums.add(THREE_MONTHS);
        enums.add(FOUR_MONTHS);
        return enums;
    }
}
