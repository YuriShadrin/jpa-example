package dev.example.jpademo.services.impl;

import static dev.example.jpademo.services.impl.DtoUtils.toCustomerDto;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.example.jpademo.dto.CustomerDto;
import dev.example.jpademo.exception.Item;
import dev.example.jpademo.exception.ItemNotFoundException;
import dev.example.jpademo.persistent.model.Customer;
import dev.example.jpademo.persistent.repository.CustomerRepository;
import dev.example.jpademo.services.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

	final private CustomerRepository repository;

	@Autowired
	public CustomerServiceImpl(CustomerRepository repository) {
		this.repository = repository;
	}

	@Override
	@Transactional(readOnly = true)
	public CustomerDto getCustomer(long id) throws ItemNotFoundException {
		return toCustomerDto(getCustomerItem(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<CustomerDto> findCustomer(long id) {
		return repository.findById(id).flatMap(c -> Optional.of(toCustomerDto(c)));
	}

	@Override
	@Transactional(readOnly = true)
	public List<CustomerDto> findCustomers(String namePattern, Pageable page) {
		return Collections.unmodifiableList(
			repository.findByNameContainingIgnoreCase(
				namePattern == null ? "" : namePattern, page
			).stream().map(DtoUtils::toCustomerDto).toList());
	}

	@Override
	@Transactional
	public CustomerDto createCustomer(String name, String address, String siteUrl) {
		return toCustomerDto(repository.save(new Customer(name, address, siteUrl)));
	}

	@Override
	@Transactional
	public CustomerDto updateCustomer(long id, String address, String siteUrl) throws ItemNotFoundException {
		var customer = getCustomerItem(id);
		Optional.ofNullable(address).ifPresent(customer::setAddress);
		Optional.ofNullable(siteUrl).ifPresent(customer::setSiteUrl);
		return toCustomerDto(repository.saveAndFlush(customer));
	}

	Customer getCustomerItem(long id) {
		return repository.findById(id).orElseThrow(() -> new ItemNotFoundException(id, Item.CUSTOMER));
	}
}
