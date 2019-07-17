package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ToDoList;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ToDoList}.
 */
public interface ToDoListService {

    /**
     * Save a toDoList.
     *
     * @param toDoList the entity to save.
     * @return the persisted entity.
     */
    ToDoList save(ToDoList toDoList);

    /**
     * Get all the toDoLists.
     *
     * @return the list of entities.
     */
    List<ToDoList> findAll();


    /**
     * Get the "id" toDoList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToDoList> findOne(Long id);

    /**
     * Delete the "id" toDoList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
