package dev.example.jpademo.persistent.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.example.jpademo.persistent.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	List<Customer> findByName(String name);

	List<Customer> findByNameContainingIgnoreCase(String firstName, Pageable page);
}
