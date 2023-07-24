package dev.example.jpademo.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import dev.example.jpademo.dto.EmployeeDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.exception.ItemNotFoundException;

public interface EmployeeService {

	EmployeeDto getEmployee(long id) throws ItemNotFoundException;

	Optional<EmployeeDto> findEmployee(long id);

	EmployeeDto createEmployee(String firstname, String lastName, Date birthDate);

	void deleteEmployee(long id);

	EmployeeDto updateEmployee(long id, String lastName);

	List<EmployeeDto> findEmployees(String lastNamePattern, Pageable page);

	void setEmployeePhoto(long employeeId, byte[] photo);

	Optional<byte[]> getEmployeePhoto(long employeeId);

	EmployeeDto assignToProject(EmployeeDto employee, ProjectDto project);

	EmployeeDto deassignFromProject(EmployeeDto employee, ProjectDto project);
	
	List<EmployeeDto> getProjectEmployees(long projectId);

	List<EmployeeDto> getCustomerEmployees(long customerId);
}
