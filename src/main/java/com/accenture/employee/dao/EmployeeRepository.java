package com.accenture.employee.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.accenture.employee.model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee,Integer>{


}
