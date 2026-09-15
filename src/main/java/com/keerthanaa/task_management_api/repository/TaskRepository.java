package com.keerthanaa.task_management_api.repository;

import com.keerthanaa.task_management_api.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
