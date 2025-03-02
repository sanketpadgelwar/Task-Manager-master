package com.craft.tmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.craft.tmanager.entity.Project;
import com.craft.tmanager.entity.Task;
import com.craft.tmanager.entity.User;

public interface TaskRepository extends JpaRepository<Task, Long> {
	
	@Query("SELECT t FROM Task t ORDER BY t.lastUpdatedOn DESC")
	List<Task> findAllOrderByLastUpdatedOnDesc();
    List<Task> findByProjectId(Project project);
    List<Task> findByAssignedUserId(User assignedUser);
	List<Task> findByProjectId(Optional<Project> findById);
}
