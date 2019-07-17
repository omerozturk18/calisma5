package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GatewayApp;
import com.mycompany.myapp.domain.ToDoItems;
import com.mycompany.myapp.domain.ToDoList;
import com.mycompany.myapp.repository.ToDoItemsRepository;
import com.mycompany.myapp.service.ToDoItemsService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Status;
/**
 * Integration tests for the {@Link ToDoItemsResource} REST controller.
 */
@SpringBootTest(classes = GatewayApp.class)
public class ToDoItemsResourceIT {

    private static final String DEFAULT_TODO_ITEM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TODO_ITEM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_DEADLINE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DEADLINE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Status DEFAULT_STATUS = Status.COMPLETED;
    private static final Status UPDATED_STATUS = Status.CONTINUES;

    @Autowired
    private ToDoItemsRepository toDoItemsRepository;

    @Autowired
    private ToDoItemsService toDoItemsService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restToDoItemsMockMvc;

    private ToDoItems toDoItems;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToDoItemsResource toDoItemsResource = new ToDoItemsResource(toDoItemsService);
        this.restToDoItemsMockMvc = MockMvcBuilders.standaloneSetup(toDoItemsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoItems createEntity(EntityManager em) {
        ToDoItems toDoItems = new ToDoItems()
            .todoItemName(DEFAULT_TODO_ITEM_NAME)
            .description(DEFAULT_DESCRIPTION)
            .deadline(DEFAULT_DEADLINE)
            .status(DEFAULT_STATUS);
        // Add required entity
        ToDoList toDoList;
        if (TestUtil.findAll(em, ToDoList.class).isEmpty()) {
            toDoList = ToDoListResourceIT.createEntity(em);
            em.persist(toDoList);
            em.flush();
        } else {
            toDoList = TestUtil.findAll(em, ToDoList.class).get(0);
        }
        toDoItems.setToDoList(toDoList);
        return toDoItems;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoItems createUpdatedEntity(EntityManager em) {
        ToDoItems toDoItems = new ToDoItems()
            .todoItemName(UPDATED_TODO_ITEM_NAME)
            .description(UPDATED_DESCRIPTION)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS);
        // Add required entity
        ToDoList toDoList;
        if (TestUtil.findAll(em, ToDoList.class).isEmpty()) {
            toDoList = ToDoListResourceIT.createUpdatedEntity(em);
            em.persist(toDoList);
            em.flush();
        } else {
            toDoList = TestUtil.findAll(em, ToDoList.class).get(0);
        }
        toDoItems.setToDoList(toDoList);
        return toDoItems;
    }

    @BeforeEach
    public void initTest() {
        toDoItems = createEntity(em);
    }

    @Test
    @Transactional
    public void createToDoItems() throws Exception {
        int databaseSizeBeforeCreate = toDoItemsRepository.findAll().size();

        // Create the ToDoItems
        restToDoItemsMockMvc.perform(post("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoItems)))
            .andExpect(status().isCreated());

        // Validate the ToDoItems in the database
        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeCreate + 1);
        ToDoItems testToDoItems = toDoItemsList.get(toDoItemsList.size() - 1);
        assertThat(testToDoItems.getTodoItemName()).isEqualTo(DEFAULT_TODO_ITEM_NAME);
        assertThat(testToDoItems.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testToDoItems.getDeadline()).isEqualTo(DEFAULT_DEADLINE);
        assertThat(testToDoItems.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createToDoItemsWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toDoItemsRepository.findAll().size();

        // Create the ToDoItems with an existing ID
        toDoItems.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoItemsMockMvc.perform(post("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoItems)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoItems in the database
        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTodoItemNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = toDoItemsRepository.findAll().size();
        // set the field null
        toDoItems.setTodoItemName(null);

        // Create the ToDoItems, which fails.

        restToDoItemsMockMvc.perform(post("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoItems)))
            .andExpect(status().isBadRequest());

        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = toDoItemsRepository.findAll().size();
        // set the field null
        toDoItems.setStatus(null);

        // Create the ToDoItems, which fails.

        restToDoItemsMockMvc.perform(post("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoItems)))
            .andExpect(status().isBadRequest());

        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllToDoItems() throws Exception {
        // Initialize the database
        toDoItemsRepository.saveAndFlush(toDoItems);

        // Get all the toDoItemsList
        restToDoItemsMockMvc.perform(get("/api/to-do-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoItems.getId().intValue())))
            .andExpect(jsonPath("$.[*].todoItemName").value(hasItem(DEFAULT_TODO_ITEM_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].deadline").value(hasItem(sameInstant(DEFAULT_DEADLINE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getToDoItems() throws Exception {
        // Initialize the database
        toDoItemsRepository.saveAndFlush(toDoItems);

        // Get the toDoItems
        restToDoItemsMockMvc.perform(get("/api/to-do-items/{id}", toDoItems.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(toDoItems.getId().intValue()))
            .andExpect(jsonPath("$.todoItemName").value(DEFAULT_TODO_ITEM_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.deadline").value(sameInstant(DEFAULT_DEADLINE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingToDoItems() throws Exception {
        // Get the toDoItems
        restToDoItemsMockMvc.perform(get("/api/to-do-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateToDoItems() throws Exception {
        // Initialize the database
        toDoItemsService.save(toDoItems);

        int databaseSizeBeforeUpdate = toDoItemsRepository.findAll().size();

        // Update the toDoItems
        ToDoItems updatedToDoItems = toDoItemsRepository.findById(toDoItems.getId()).get();
        // Disconnect from session so that the updates on updatedToDoItems are not directly saved in db
        em.detach(updatedToDoItems);
        updatedToDoItems
            .todoItemName(UPDATED_TODO_ITEM_NAME)
            .description(UPDATED_DESCRIPTION)
            .deadline(UPDATED_DEADLINE)
            .status(UPDATED_STATUS);

        restToDoItemsMockMvc.perform(put("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedToDoItems)))
            .andExpect(status().isOk());

        // Validate the ToDoItems in the database
        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeUpdate);
        ToDoItems testToDoItems = toDoItemsList.get(toDoItemsList.size() - 1);
        assertThat(testToDoItems.getTodoItemName()).isEqualTo(UPDATED_TODO_ITEM_NAME);
        assertThat(testToDoItems.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testToDoItems.getDeadline()).isEqualTo(UPDATED_DEADLINE);
        assertThat(testToDoItems.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingToDoItems() throws Exception {
        int databaseSizeBeforeUpdate = toDoItemsRepository.findAll().size();

        // Create the ToDoItems

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoItemsMockMvc.perform(put("/api/to-do-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoItems)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoItems in the database
        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteToDoItems() throws Exception {
        // Initialize the database
        toDoItemsService.save(toDoItems);

        int databaseSizeBeforeDelete = toDoItemsRepository.findAll().size();

        // Delete the toDoItems
        restToDoItemsMockMvc.perform(delete("/api/to-do-items/{id}", toDoItems.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDoItems> toDoItemsList = toDoItemsRepository.findAll();
        assertThat(toDoItemsList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoItems.class);
        ToDoItems toDoItems1 = new ToDoItems();
        toDoItems1.setId(1L);
        ToDoItems toDoItems2 = new ToDoItems();
        toDoItems2.setId(toDoItems1.getId());
        assertThat(toDoItems1).isEqualTo(toDoItems2);
        toDoItems2.setId(2L);
        assertThat(toDoItems1).isNotEqualTo(toDoItems2);
        toDoItems1.setId(null);
        assertThat(toDoItems1).isNotEqualTo(toDoItems2);
    }
}
