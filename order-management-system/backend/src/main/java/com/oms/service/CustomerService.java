package com.oms.service;

import com.oms.dto.CustomerRequest;
import com.oms.dto.CustomerResponse;
import com.oms.model.Customer;
import com.oms.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository repository;

    public List<CustomerResponse> findAll() {
        return repository.findAll().stream().map(CustomerResponse::from).collect(Collectors.toList());
    }

    public CustomerResponse findById(UUID id) {
        return CustomerResponse.from(getOrThrow(id));
    }

    @Transactional
    public CustomerResponse create(CustomerRequest req) {
        if (repository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        Customer c = new Customer();
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());
        c.setAddress(req.getAddress());
        return CustomerResponse.from(repository.save(c));
    }

    @Transactional
    public CustomerResponse update(UUID id, CustomerRequest req) {
        Customer c = getOrThrow(id);
        if (!c.getEmail().equals(req.getEmail()) && repository.existsByEmail(req.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }
        c.setName(req.getName());
        c.setEmail(req.getEmail());
        c.setPhone(req.getPhone());
        c.setAddress(req.getAddress());
        return CustomerResponse.from(repository.save(c));
    }

    @Transactional
    public void delete(UUID id) {
        getOrThrow(id);
        repository.deleteById(id);
    }

    Customer getOrThrow(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    }
}
