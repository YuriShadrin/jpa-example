package dev.example.jpademo.persistent.model;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "EMPLOYEE_PHOTO")
public class EmployeePhoto {

	@Id
	@Column(nullable = false, name = "id")
	private Long employeeId;

	@Lob
	@Column(nullable = false)
	private byte[] photo;

	protected EmployeePhoto() {
	}

	public EmployeePhoto(Long employeeId, byte[] photo) {
		this.employeeId = employeeId;
		this.photo = photo;
	}

	public byte[] getPhoto() {
		return photo;
	}

	@Override
	public String toString() {
		return "EmployeePhoto [id=" + employeeId + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(employeeId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePhoto other = (EmployeePhoto) obj;
		return Objects.equals(employeeId, other.employeeId);
	}

}
