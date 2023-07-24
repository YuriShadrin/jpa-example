package dev.example.jpademo.dto;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public record EmployeeDto(long id, String firstName, String lastName, Date birthDate, List<ProjectDto>projects) {

	public EmployeeDto(long id, String firstName, String lastName, Date birthDate) {
		this(id, firstName, lastName, birthDate, Collections.emptyList());
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, birthDate);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeeDto other = (EmployeeDto) obj;
		return  id == other.id &&
				Objects.equals(firstName, other.firstName) &&
				Objects.equals(lastName, other.lastName) &&
				Objects.equals(birthDate, other.birthDate);
	}

}
