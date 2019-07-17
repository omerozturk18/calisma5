package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ToDoItemsService;
import com.mycompany.myapp.domain.ToDoItems;
import com.mycompany.myapp.repository.ToDoItemsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ToDoItems}.
 */
@Service
@Transactional
public class ToDoItemsServiceImpl implements ToDoItemsService {

    private final Logger log = LoggerFactory.getLogger(ToDoItemsServiceImpl.class);

    private final ToDoItemsRepository toDoItemsRepository;

    public ToDoItemsServiceImpl(ToDoItemsRepository toDoItemsRepository) {
        this.toDoItemsRepository = toDoItemsRepository;
    }

    /**
     * Save a toDoItems.
     *
     * @param toDoItems the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ToDoItems save(ToDoItems toDoItems) {
        log.debug("Request to save ToDoItems : {}", toDoItems);
        return toDoItemsRepository.save(toDoItems);
    }

    /**
     * Get all the toDoItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ToDoItems> findAll(Pageable pageable) {
        log.debug("Request to get all ToDoItems");
        return toDoItemsRepository.findAll(pageable);
    }


    /**
     * Get one toDoItems by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ToDoItems> findOne(Long id) {
        log.debug("Request to get ToDoItems : {}", id);
        return toDoItemsRepository.findById(id);
    }

    /**
     * Delete the toDoItems by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToDoItems : {}", id);
        toDoItemsRepository.deleteById(id);
    }
}
