package com.mcnedward.keepfit.utils.exceptions;

/**
 * Created by Edward on 2/13/2016.
 */
public class EntityDoesNotExistException extends Exception {

    public EntityDoesNotExistException(long id) {
        super("Entity with the id " + id + " does not exist.");
    }

}
