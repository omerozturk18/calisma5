package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ToDoList;
import com.mycompany.myapp.service.ToDoListService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ToDoList}.
 */
@RestController
@RequestMapping("/api")
public class ToDoListResource {

    private final Logger log = LoggerFactory.getLogger(ToDoListResource.class);

    private static final String ENTITY_NAME = "toDoList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoListService toDoListService;

    public ToDoListResource(ToDoListService toDoListService) {
        this.toDoListService = toDoListService;
    }

    /**
     * {@code POST  /to-do-lists} : Create a new toDoList.
     *
     * @param toDoList the toDoList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDoList, or with status {@code 400 (Bad Request)} if the toDoList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-do-lists")
    public ResponseEntity<ToDoList> createToDoList(@Valid @RequestBody ToDoList toDoList) throws URISyntaxException {
        log.debug("REST request to save ToDoList : {}", toDoList);
        if (toDoList.getId() != null) {
            throw new BadRequestAlertException("A new toDoList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToDoList result = toDoListService.save(toDoList);
        return ResponseEntity.created(new URI("/api/to-do-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-do-lists} : Updates an existing toDoList.
     *
     * @param toDoList the toDoList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoList,
     * or with status {@code 400 (Bad Request)} if the toDoList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDoList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-do-lists")
    public ResponseEntity<ToDoList> updateToDoList(@Valid @RequestBody ToDoList toDoList) throws URISyntaxException {
        log.debug("REST request to update ToDoList : {}", toDoList);
        if (toDoList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToDoList result = toDoListService.save(toDoList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoList.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /to-do-lists} : get all the toDoLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDoLists in body.
     */
    @GetMapping("/to-do-lists")
    public List<ToDoList> getAllToDoLists() {
        log.debug("REST request to get all ToDoLists");
        return toDoListService.findAll();
    }

    /**
     * {@code GET  /to-do-lists/:id} : get the "id" toDoList.
     *
     * @param id the id of the toDoList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDoList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-do-lists/{id}")
    public ResponseEntity<ToDoList> getToDoList(@PathVariable Long id) {
        log.debug("REST request to get ToDoList : {}", id);
        Optional<ToDoList> toDoList = toDoListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toDoList);
    }

    /**
     * {@code DELETE  /to-do-lists/:id} : delete the "id" toDoList.
     *
     * @param id the id of the toDoList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-do-lists/{id}")
    public ResponseEntity<Void> deleteToDoList(@PathVariable Long id) {
        log.debug("REST request to delete ToDoList : {}", id);
        toDoListService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
