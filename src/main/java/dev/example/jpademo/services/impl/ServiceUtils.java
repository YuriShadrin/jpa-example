package dev.example.jpademo.services.impl;

import java.util.Collections;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.dto.EmployeeDto;
import dev.example.jpademo.dto.ProjectDto;
import dev.example.jpademo.persistent.model.Customer;
import dev.example.jpademo.persistent.model.Employee;
import dev.example.jpademo.persistent.model.Project;

public class ServiceUtils {

	private ServiceUtils() {}

	static CustomerDto toCustomerDto(Customer customer) {
		return new CustomerDto(customer.getId(), customer.getName(), customer.getAddress(), customer.getSiteUrl());
	}

	static ProjectDto toProjectDto(Project project) {
		return toProjectDto(project, false);
	}

	static ProjectDto toProjectDto(Project project, boolean withEmployees) {
		return new ProjectDto(project.getId(), project.getName(), project.getDescription(),
			toCustomerDto(project.getCustomer()),
			withEmployees ? 
				Collections.unmodifiableList(project.getEmployees().stream().map(ServiceUtils::toEmployeeDto).toList()) :
				Collections.emptyList());
	}

	static EmployeeDto toEmployeeDto(Employee employee) {
		return new EmployeeDto(
			employee.getId(),
			employee.getFirstName(),
			employee.getLastName(),
			employee.getBirthDate(),
			Collections.unmodifiableList(employee.getProjects().stream().map(ServiceUtils::toProjectDto).toList())
		);
	}

}
