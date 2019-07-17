package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ToDoListService;
import com.mycompany.myapp.domain.ToDoList;
import com.mycompany.myapp.repository.ToDoListRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ToDoList}.
 */
@Service
@Transactional
public class ToDoListServiceImpl implements ToDoListService {

    private final Logger log = LoggerFactory.getLogger(ToDoListServiceImpl.class);

    private final ToDoListRepository toDoListRepository;

    public ToDoListServiceImpl(ToDoListRepository toDoListRepository) {
        this.toDoListRepository = toDoListRepository;
    }

    /**
     * Save a toDoList.
     *
     * @param toDoList the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ToDoList save(ToDoList toDoList) {
        log.debug("Request to save ToDoList : {}", toDoList);
        return toDoListRepository.save(toDoList);
    }

    /**
     * Get all the toDoLists.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ToDoList> findAll() {
        log.debug("Request to get all ToDoLists");
        return toDoListRepository.findAll();
    }


    /**
     * Get one toDoList by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ToDoList> findOne(Long id) {
        log.debug("Request to get ToDoList : {}", id);
        return toDoListRepository.findById(id);
    }

    /**
     * Delete the toDoList by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ToDoList : {}", id);
        toDoListRepository.deleteById(id);
    }
}
