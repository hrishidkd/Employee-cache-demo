package com.emp.cache.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emp.cache.entity.Employees;

public interface EmployeeRepository extends JpaRepository<Employees, Long> {

}
