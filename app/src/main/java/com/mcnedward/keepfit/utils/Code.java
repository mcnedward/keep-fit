package com.mcnedward.keepfit.utils;

/**
 * Created by Edward on 2/23/2016.
 */
public enum Code {

    RESULT_OK(200),
    RESULT_FAIL(400);

    private int id;

    Code(int id) {
        this.id = id;
    }

    public int id() {
        return id;
    }
}
