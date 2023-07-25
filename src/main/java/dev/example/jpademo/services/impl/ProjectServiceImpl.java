package dev.example.jpademo.services.impl;

import static dev.example.jpademo.services.impl.ServiceUtils.toProjectDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.exception.Item;
import dev.example.jpademo.exception.ItemNotFoundException;
import dev.example.jpademo.exception.PersistentExceprion;
import dev.example.jpademo.exception.ProjectNotEmptyException;
import dev.example.jpademo.persistent.model.Project;
import dev.example.jpademo.persistent.repository.ProjectRepository;
import dev.example.jpademo.services.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService {

	final private ProjectRepository repository;
	final private CustomerServiceImpl customerService;

	@Autowired
	public ProjectServiceImpl(ProjectRepository repository, CustomerServiceImpl customerService) {
		this.repository = repository;
		this.customerService = customerService;
	}

	@Override
	@Transactional(readOnly = true)
	public ProjectDto getProject(long id) throws ItemNotFoundException {
		return toProjectDto(getProjectItem(id), true);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ProjectDto> findProject(long id) {
		return repository.findById(id).flatMap(p -> Optional.of(toProjectDto(p)));
	}

	@Override
	@Transactional
	public ProjectDto createProject(String name, String description, CustomerDto customer) {
		return toProjectDto(
			repository.saveAndFlush(new Project(name, description, customerService.getCustomerItem(customer.id()))));
	}

	@Override
	@Transactional
	public void deleteProject(long id) throws PersistentExceprion {
		var proj = getProjectItem(id);
		if(proj.getEmployees().size() > 0) {
			throw new ProjectNotEmptyException(
				id,
				String.format("Cannot delete project '%s' because %d employees have been assigned.", proj.getName(), proj.getEmployees().size())
			);
		}
		repository.deleteById(id);
	}

	@Override
	@Transactional
	public ProjectDto updateProject(long id, String name, String description) throws ItemNotFoundException {
		var project = getProjectItem(id);
		Optional.ofNullable(name).ifPresent(project::setName);
		Optional.ofNullable(description).ifPresent(project::setDescription);
		return toProjectDto(repository.saveAndFlush(project));
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProjectDto> findProjects(String name, boolean withEmployees) {
		return Collections.unmodifiableList(
			repository.findByNameContainingIgnoreCase(
				name == null ? "": name
			).stream().map(p -> toProjectDto(p, withEmployees)).toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProjectDto> getCustomerProjects(long customerId) {
		return Collections.unmodifiableList(
			repository.getCustomerProjects(customerId).
			stream().map(ServiceUtils::toProjectDto).toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProjectDto> getEmployeeProjects(long employeeId) {
		return Collections.unmodifiableList(
				repository.getEmployeeProjects(employeeId).
				stream().map(ServiceUtils::toProjectDto).toList());
	}

	Project getProjectItem(long id) {
		return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id, Item.PROJECT));
	}
}
