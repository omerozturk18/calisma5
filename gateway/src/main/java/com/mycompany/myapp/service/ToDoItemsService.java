package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ToDoItems;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ToDoItems}.
 */
public interface ToDoItemsService {

    /**
     * Save a toDoItems.
     *
     * @param toDoItems the entity to save.
     * @return the persisted entity.
     */
    ToDoItems save(ToDoItems toDoItems);

    /**
     * Get all the toDoItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ToDoItems> findAll(Pageable pageable);


    /**
     * Get the "id" toDoItems.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ToDoItems> findOne(Long id);

    /**
     * Delete the "id" toDoItems.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
