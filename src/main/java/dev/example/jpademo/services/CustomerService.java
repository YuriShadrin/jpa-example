package dev.example.jpademo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.exception.ItemNotFoundException;

public interface CustomerService {

	CustomerDto getCustomer(long id) throws ItemNotFoundException;

	Optional<CustomerDto> findCustomer(long id);

	List<CustomerDto> findCustomers(String namePattern, Pageable page);

	CustomerDto createCustomer(String name, String address, String siteUrl);

	CustomerDto updateCustomer(long id, String address, String siteUrl) throws ItemNotFoundException;
}
