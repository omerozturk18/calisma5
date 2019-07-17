package com.mycompany.myapp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ToDoList.
 */
@Entity
@Table(name = "to_do_list")
public class ToDoList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "todo_list_name", nullable = false)
    private String todoListName;

    @OneToMany(mappedBy = "toDoList")
    private Set<ToDoItems> toDoListToDoItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTodoListName() {
        return todoListName;
    }

    public ToDoList todoListName(String todoListName) {
        this.todoListName = todoListName;
        return this;
    }

    public void setTodoListName(String todoListName) {
        this.todoListName = todoListName;
    }

    public Set<ToDoItems> getToDoListToDoItems() {
        return toDoListToDoItems;
    }

    public ToDoList toDoListToDoItems(Set<ToDoItems> toDoItems) {
        this.toDoListToDoItems = toDoItems;
        return this;
    }

    public ToDoList addToDoListToDoItems(ToDoItems toDoItems) {
        this.toDoListToDoItems.add(toDoItems);
        toDoItems.setToDoList(this);
        return this;
    }

    public ToDoList removeToDoListToDoItems(ToDoItems toDoItems) {
        this.toDoListToDoItems.remove(toDoItems);
        toDoItems.setToDoList(null);
        return this;
    }

    public void setToDoListToDoItems(Set<ToDoItems> toDoItems) {
        this.toDoListToDoItems = toDoItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoList)) {
            return false;
        }
        return id != null && id.equals(((ToDoList) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ToDoList{" +
            "id=" + getId() +
            ", todoListName='" + getTodoListName() + "'" +
            "}";
    }
}
