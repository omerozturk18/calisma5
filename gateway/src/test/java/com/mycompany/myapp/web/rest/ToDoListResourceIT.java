package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.GatewayApp;
import com.mycompany.myapp.domain.ToDoList;
import com.mycompany.myapp.repository.ToDoListRepository;
import com.mycompany.myapp.service.ToDoListService;
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
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ToDoListResource} REST controller.
 */
@SpringBootTest(classes = GatewayApp.class)
public class ToDoListResourceIT {

    private static final String DEFAULT_TODO_LIST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TODO_LIST_NAME = "BBBBBBBBBB";

    @Autowired
    private ToDoListRepository toDoListRepository;

    @Autowired
    private ToDoListService toDoListService;

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

    private MockMvc restToDoListMockMvc;

    private ToDoList toDoList;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ToDoListResource toDoListResource = new ToDoListResource(toDoListService);
        this.restToDoListMockMvc = MockMvcBuilders.standaloneSetup(toDoListResource)
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
    public static ToDoList createEntity(EntityManager em) {
        ToDoList toDoList = new ToDoList()
            .todoListName(DEFAULT_TODO_LIST_NAME);
        return toDoList;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ToDoList createUpdatedEntity(EntityManager em) {
        ToDoList toDoList = new ToDoList()
            .todoListName(UPDATED_TODO_LIST_NAME);
        return toDoList;
    }

    @BeforeEach
    public void initTest() {
        toDoList = createEntity(em);
    }

    @Test
    @Transactional
    public void createToDoList() throws Exception {
        int databaseSizeBeforeCreate = toDoListRepository.findAll().size();

        // Create the ToDoList
        restToDoListMockMvc.perform(post("/api/to-do-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoList)))
            .andExpect(status().isCreated());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeCreate + 1);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getTodoListName()).isEqualTo(DEFAULT_TODO_LIST_NAME);
    }

    @Test
    @Transactional
    public void createToDoListWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = toDoListRepository.findAll().size();

        // Create the ToDoList with an existing ID
        toDoList.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restToDoListMockMvc.perform(post("/api/to-do-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoList)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTodoListNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = toDoListRepository.findAll().size();
        // set the field null
        toDoList.setTodoListName(null);

        // Create the ToDoList, which fails.

        restToDoListMockMvc.perform(post("/api/to-do-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoList)))
            .andExpect(status().isBadRequest());

        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllToDoLists() throws Exception {
        // Initialize the database
        toDoListRepository.saveAndFlush(toDoList);

        // Get all the toDoListList
        restToDoListMockMvc.perform(get("/api/to-do-lists?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(toDoList.getId().intValue())))
            .andExpect(jsonPath("$.[*].todoListName").value(hasItem(DEFAULT_TODO_LIST_NAME.toString())));
    }
    
    @Test
    @Transactional
    public void getToDoList() throws Exception {
        // Initialize the database
        toDoListRepository.saveAndFlush(toDoList);

        // Get the toDoList
        restToDoListMockMvc.perform(get("/api/to-do-lists/{id}", toDoList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(toDoList.getId().intValue()))
            .andExpect(jsonPath("$.todoListName").value(DEFAULT_TODO_LIST_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingToDoList() throws Exception {
        // Get the toDoList
        restToDoListMockMvc.perform(get("/api/to-do-lists/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateToDoList() throws Exception {
        // Initialize the database
        toDoListService.save(toDoList);

        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();

        // Update the toDoList
        ToDoList updatedToDoList = toDoListRepository.findById(toDoList.getId()).get();
        // Disconnect from session so that the updates on updatedToDoList are not directly saved in db
        em.detach(updatedToDoList);
        updatedToDoList
            .todoListName(UPDATED_TODO_LIST_NAME);

        restToDoListMockMvc.perform(put("/api/to-do-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedToDoList)))
            .andExpect(status().isOk());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
        ToDoList testToDoList = toDoListList.get(toDoListList.size() - 1);
        assertThat(testToDoList.getTodoListName()).isEqualTo(UPDATED_TODO_LIST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingToDoList() throws Exception {
        int databaseSizeBeforeUpdate = toDoListRepository.findAll().size();

        // Create the ToDoList

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restToDoListMockMvc.perform(put("/api/to-do-lists")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(toDoList)))
            .andExpect(status().isBadRequest());

        // Validate the ToDoList in the database
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteToDoList() throws Exception {
        // Initialize the database
        toDoListService.save(toDoList);

        int databaseSizeBeforeDelete = toDoListRepository.findAll().size();

        // Delete the toDoList
        restToDoListMockMvc.perform(delete("/api/to-do-lists/{id}", toDoList.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ToDoList> toDoListList = toDoListRepository.findAll();
        assertThat(toDoListList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ToDoList.class);
        ToDoList toDoList1 = new ToDoList();
        toDoList1.setId(1L);
        ToDoList toDoList2 = new ToDoList();
        toDoList2.setId(toDoList1.getId());
        assertThat(toDoList1).isEqualTo(toDoList2);
        toDoList2.setId(2L);
        assertThat(toDoList1).isNotEqualTo(toDoList2);
        toDoList1.setId(null);
        assertThat(toDoList1).isNotEqualTo(toDoList2);
    }
}
