package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.ToDoItems;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ToDoItems entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToDoItemsRepository extends JpaRepository<ToDoItems, Long> {

}
