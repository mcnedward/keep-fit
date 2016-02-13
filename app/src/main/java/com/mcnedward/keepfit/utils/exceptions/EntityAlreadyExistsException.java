package com.mcnedward.keepfit.utils.exceptions;

/**
 * Created by Edward on 2/13/2016.
 */
public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(long id) {
        super("An entity with the id " + id + " already exists.");
    }
    
}
