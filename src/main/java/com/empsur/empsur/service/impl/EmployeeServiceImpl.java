package com.empsur.empsur.service.impl;

import com.empsur.empsur.domain.Employee;
import com.empsur.empsur.repository.EmployeeRepository;
import com.empsur.empsur.service.EmployeeService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Employee update(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        return employeeRepository.save(employee);
    }

    @Override
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(existingEmployee -> {
                if (employee.getFirstName() != null) {
                    existingEmployee.setFirstName(employee.getFirstName());
                }
                if (employee.getLastName() != null) {
                    existingEmployee.setLastName(employee.getLastName());
                }
                if (employee.getFullName() != null) {
                    existingEmployee.setFullName(employee.getFullName());
                }
                if (employee.getGender() != null) {
                    existingEmployee.setGender(employee.getGender());
                }
                if (employee.getStatus() != null) {
                    existingEmployee.setStatus(employee.getStatus());
                }
                if (employee.getPhone() != null) {
                    existingEmployee.setPhone(employee.getPhone());
                }
                if (employee.getAddressLine1() != null) {
                    existingEmployee.setAddressLine1(employee.getAddressLine1());
                }
                if (employee.getAddressLine2() != null) {
                    existingEmployee.setAddressLine2(employee.getAddressLine2());
                }
                if (employee.getCity() != null) {
                    existingEmployee.setCity(employee.getCity());
                }
                if (employee.getCountry() != null) {
                    existingEmployee.setCountry(employee.getCountry());
                }
                if (employee.getLicenseNumber() != null) {
                    existingEmployee.setLicenseNumber(employee.getLicenseNumber());
                }

                return existingEmployee;
            })
            .map(employeeRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    public Page<Employee> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the employees where Record is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Employee> findAllWhereRecordIsNull() {
        log.debug("Request to get all employees where Record is null");
        return StreamSupport
            .stream(employeeRepository.findAll().spliterator(), false)
            .filter(employee -> employee.getRecord() == null)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
