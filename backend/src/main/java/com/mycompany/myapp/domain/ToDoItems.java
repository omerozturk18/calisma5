package com.mycompany.myapp.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;

import com.mycompany.myapp.domain.enumeration.Status;

/**
 * A ToDoItems.
 */
@Entity
@Table(name = "to_do_items")
public class ToDoItems implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "todo_item_name", nullable = false)
    private String todoItemName;

    @Column(name = "description")
    private String description;

    @Column(name = "deadline")
    private ZonedDateTime deadline;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("toDoListToDoItems")
    private ToDoList toDoList;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTodoItemName() {
        return todoItemName;
    }

    public ToDoItems todoItemName(String todoItemName) {
        this.todoItemName = todoItemName;
        return this;
    }

    public void setTodoItemName(String todoItemName) {
        this.todoItemName = todoItemName;
    }

    public String getDescription() {
        return description;
    }

    public ToDoItems description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ZonedDateTime getDeadline() {
        return deadline;
    }

    public ToDoItems deadline(ZonedDateTime deadline) {
        this.deadline = deadline;
        return this;
    }

    public void setDeadline(ZonedDateTime deadline) {
        this.deadline = deadline;
    }

    public Status getStatus() {
        return status;
    }

    public ToDoItems status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ToDoList getToDoList() {
        return toDoList;
    }

    public ToDoItems toDoList(ToDoList toDoList) {
        this.toDoList = toDoList;
        return this;
    }

    public void setToDoList(ToDoList toDoList) {
        this.toDoList = toDoList;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToDoItems)) {
            return false;
        }
        return id != null && id.equals(((ToDoItems) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ToDoItems{" +
            "id=" + getId() +
            ", todoItemName='" + getTodoItemName() + "'" +
            ", description='" + getDescription() + "'" +
            ", deadline='" + getDeadline() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
