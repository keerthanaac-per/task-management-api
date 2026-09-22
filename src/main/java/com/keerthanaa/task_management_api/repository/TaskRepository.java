package com.keerthanaa.task_management_api.repository;

import com.keerthanaa.task_management_api.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Page<Task> findByTitleContainingIgnoreCase(
            String title,
            Pageable pageable
    );
}