package dev.example.jpademo.services.impl;

import static dev.example.jpademo.services.impl.ServiceUtils.toEmployeeDto;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.example.jpademo.dto.EmployeeDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.exception.Item;
import dev.example.jpademo.exception.ItemNotFoundException;
import dev.example.jpademo.persistent.model.Employee;
import dev.example.jpademo.persistent.model.EmployeePhoto;
import dev.example.jpademo.persistent.repository.EmployeePhotoRepository;
import dev.example.jpademo.persistent.repository.EmployeeRepository;
import dev.example.jpademo.services.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	final private EmployeeRepository employeeRepository;
	final private EmployeePhotoRepository photoRepository;
	final private ProjectServiceImpl projectService; 

	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeePhotoRepository photoRepository, ProjectServiceImpl projectService) {
		this.employeeRepository = employeeRepository;
		this.photoRepository = photoRepository;
		this.projectService = projectService;
	}

	@Override
	@Transactional(readOnly = true)
	public EmployeeDto getEmployee(long id) throws ItemNotFoundException {
		return toEmployeeDto(getEmployeeItem(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<EmployeeDto> findEmployee(long id) {
		return employeeRepository.findById(id).flatMap(c -> Optional.of(toEmployeeDto(c)));
	}

	@Override
	@Transactional
	public EmployeeDto createEmployee(String firstname, String lastName, Date birthDate) {
		return toEmployeeDto(employeeRepository.saveAndFlush(new Employee(firstname, lastName, birthDate)));
	}

	@Override
	@Transactional
	public void deleteEmployee(long id) {
		var empl = getEmployee(id);
		projectService.getEmployeeProjects(id).forEach(proj -> deassignFromProject(empl, proj));
		photoRepository.deleteById(id);
		employeeRepository.deleteById(id);
	}

	@Override
	@Transactional
	public EmployeeDto updateEmployee(long id, String lastName) {
		var employee = getEmployeeItem(id);
		Optional.ofNullable(lastName).ifPresent(employee::setLastName);
		return toEmployeeDto(employeeRepository.saveAndFlush(employee));
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDto> findEmployees(String lastNamePattern, Pageable page) {
		Objects.requireNonNull(lastNamePattern, "lastNamePattern cannot be null");
		return Collections.unmodifiableList(
			employeeRepository.findByLastNameContainingIgnoreCase(lastNamePattern, page).
			stream().map(ServiceUtils::toEmployeeDto).toList());
	}

	@Override
	@Transactional
	public void setEmployeePhoto(long employeeId, byte[] photo) {
		var empl = getEmployee(employeeId);
		photoRepository.save(new EmployeePhoto(empl.id(), photo));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<byte[]> getEmployeePhoto(long employeeId) {
		return photoRepository.findById(employeeId).flatMap(p -> Optional.of(p.getPhoto()));
	}

	@Override
	@Transactional
	public EmployeeDto assignToProject(EmployeeDto employee, ProjectDto project) {
		return Optional.of(employee)
			.map(e -> getEmployeeItem(e.id()))
			.map(e -> e.assignToProject(projectService.getProjectItem(project.id())))
			.map(employeeRepository::save)
			.map(ServiceUtils::toEmployeeDto)
			.get();
	}

	@Override
	@Transactional
	public EmployeeDto deassignFromProject(EmployeeDto employee, ProjectDto project) {
		return Optional.of(employee)
			.map(e -> getEmployeeItem(e.id()))
			.map(e -> e.deassignFromProject(projectService.getProjectItem(project.id())))
			.map(employeeRepository::save)
			.map(ServiceUtils::toEmployeeDto).get();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDto> getProjectEmployees(long projectId) {
		return Collections.unmodifiableList(
			employeeRepository.getProjectEmployees(projectId).
			stream().map(ServiceUtils::toEmployeeDto).toList());
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeDto> getCustomerEmployees(long customerId) {
		return Collections.unmodifiableList(
			employeeRepository.getCustomerEmployees(customerId).
			stream().map(ServiceUtils::toEmployeeDto).toList());
	}

	Employee getEmployeeItem(long id) {
		return employeeRepository.findById(id).orElseThrow(() -> new ItemNotFoundException(id, Item.EMPLOYEE));
	}

}
