package dev.example.jpademo.persistent.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import dev.example.jpademo.persistent.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findByLastNameContainingIgnoreCase(String lastName, Pageable page);

	@Query("SELECT e FROM Employee e JOIN e.projects p WHERE p.id = :projectId")
	List<Employee> getProjectEmployees(long projectId);

	@Query("SELECT e FROM Employee e JOIN e.projects p JOIN p.customer c WHERE c.id = :customerId")
	List<Employee> getCustomerEmployees(long customerId);
}
