package dev.example.jpademo.persistent.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import dev.example.jpademo.persistent.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

	@Query("SELECT p FROM Project p WHERE p.customer.id = :customerId")
	List<Project> getCustomerProjects(@Param("customerId") long customerId);

	List<Project> findByNameContainingIgnoreCase(String name);

	@Query("SELECT p FROM Project p JOIN p.employees e WHERE e.id = :employeeId")
	List<Project> getEmployeeProjects(long employeeId);

	// Explicitly deleting due to Project.customer FetchType = EAGER 
	@Modifying(flushAutomatically = true)
	@Query("DELETE FROM Project p WHERE p.id =:projectId")
	void deleteById(long projectId);
}
