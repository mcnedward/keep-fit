package com.mcnedward.keepfit.repository;

import com.mcnedward.keepfit.utils.exceptions.EntityAlreadyExistsException;
import com.mcnedward.keepfit.utils.exceptions.EntityDoesNotExistException;

import java.util.List;

/**
 * Created by Edward on 2/8/2016.
 */
public interface IRepository<T> {

    T get(int id);

    T get(String... args);

    T save(T entity) throws EntityAlreadyExistsException;

    boolean update(T entity) throws EntityDoesNotExistException;

    boolean delete(T entity) throws EntityDoesNotExistException;

    List<T> retrieve();
}
