package dev.example.jpademo.services;

import java.util.List;
import java.util.Optional;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.exception.ItemNotFoundException;
import dev.example.jpademo.exception.ProjectNotEmptyException;

public interface ProjectService {

	ProjectDto getProject(long id) throws ItemNotFoundException;

	Optional<ProjectDto> findProject(long id);

	ProjectDto createProject(String name, String description, CustomerDto customer);
	
	void deleteProject(long id) throws ItemNotFoundException, ProjectNotEmptyException;

	ProjectDto updateProject(long id, String name, String description) throws ItemNotFoundException;

	List<ProjectDto> findProjects(String name, boolean withEmployees);

	List<ProjectDto> getCustomerProjects(long customerId);

	List<ProjectDto> getEmployeeProjects(long employeeId);
}
