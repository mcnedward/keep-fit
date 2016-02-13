package com.mcnedward.keepfit.model;

import java.io.Serializable;

/**
 * Created by Edward on 2/13/2016.
 */
public abstract class BaseEntity implements Serializable {

    protected Long id = null;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
