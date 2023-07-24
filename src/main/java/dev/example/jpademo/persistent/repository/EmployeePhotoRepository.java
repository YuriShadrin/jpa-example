package dev.example.jpademo.persistent.repository;

import org.springframework.data.repository.CrudRepository;

import dev.example.jpademo.persistent.model.EmployeePhoto;

public interface EmployeePhotoRepository extends CrudRepository<EmployeePhoto, Long> {
}
