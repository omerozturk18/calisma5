package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ToDoItems;
import com.mycompany.myapp.service.ToDoItemsService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ToDoItems}.
 */
@RestController
@RequestMapping("/api")
public class ToDoItemsResource {

    private final Logger log = LoggerFactory.getLogger(ToDoItemsResource.class);

    private static final String ENTITY_NAME = "backendToDoItems";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToDoItemsService toDoItemsService;

    public ToDoItemsResource(ToDoItemsService toDoItemsService) {
        this.toDoItemsService = toDoItemsService;
    }

    /**
     * {@code POST  /to-do-items} : Create a new toDoItems.
     *
     * @param toDoItems the toDoItems to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new toDoItems, or with status {@code 400 (Bad Request)} if the toDoItems has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/to-do-items")
    public ResponseEntity<ToDoItems> createToDoItems(@Valid @RequestBody ToDoItems toDoItems) throws URISyntaxException {
        log.debug("REST request to save ToDoItems : {}", toDoItems);
        if (toDoItems.getId() != null) {
            throw new BadRequestAlertException("A new toDoItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ToDoItems result = toDoItemsService.save(toDoItems);
        return ResponseEntity.created(new URI("/api/to-do-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /to-do-items} : Updates an existing toDoItems.
     *
     * @param toDoItems the toDoItems to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated toDoItems,
     * or with status {@code 400 (Bad Request)} if the toDoItems is not valid,
     * or with status {@code 500 (Internal Server Error)} if the toDoItems couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/to-do-items")
    public ResponseEntity<ToDoItems> updateToDoItems(@Valid @RequestBody ToDoItems toDoItems) throws URISyntaxException {
        log.debug("REST request to update ToDoItems : {}", toDoItems);
        if (toDoItems.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ToDoItems result = toDoItemsService.save(toDoItems);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, toDoItems.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /to-do-items} : get all the toDoItems.
     *
     * @param pageable the pagination information.
     * @param queryParams a {@link MultiValueMap} query parameters.
     * @param uriBuilder a {@link UriComponentsBuilder} URI builder.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of toDoItems in body.
     */
    @GetMapping("/to-do-items")
    public ResponseEntity<List<ToDoItems>> getAllToDoItems(Pageable pageable, @RequestParam MultiValueMap<String, String> queryParams, UriComponentsBuilder uriBuilder) {
        log.debug("REST request to get a page of ToDoItems");
        Page<ToDoItems> page = toDoItemsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(uriBuilder.queryParams(queryParams), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /to-do-items/:id} : get the "id" toDoItems.
     *
     * @param id the id of the toDoItems to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the toDoItems, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/to-do-items/{id}")
    public ResponseEntity<ToDoItems> getToDoItems(@PathVariable Long id) {
        log.debug("REST request to get ToDoItems : {}", id);
        Optional<ToDoItems> toDoItems = toDoItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(toDoItems);
    }

    /**
     * {@code DELETE  /to-do-items/:id} : delete the "id" toDoItems.
     *
     * @param id the id of the toDoItems to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/to-do-items/{id}")
    public ResponseEntity<Void> deleteToDoItems(@PathVariable Long id) {
        log.debug("REST request to delete ToDoItems : {}", id);
        toDoItemsService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
