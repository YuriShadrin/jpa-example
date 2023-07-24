package dev.example.jpademo.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record ProjectDto(long id, String name, String description, CustomerDto customer, List<EmployeeDto> employees) {

	public ProjectDto(long id, String name, String description, CustomerDto customer) {
		this(id, name, description, customer, Collections.emptyList());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProjectDto other = (ProjectDto) obj;
		return  id == other.id &&
				Objects.equals(name, other.name) &&
				Objects.equals(description, other.description);
	}

}
