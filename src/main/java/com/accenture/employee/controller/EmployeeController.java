package com.accenture.employee.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.accenture.employee.model.Employee;
import com.accenture.employee.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@RequestMapping(value = "emp/controller/listEmployees", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Employee>> listEmployeeDetails() {
		List<Employee> listEmployee = new ArrayList<Employee>(employeeService.listEmployeeDetails());

		return new ResponseEntity<List<Employee>>(listEmployee,HttpStatus.OK);
	}

	@RequestMapping(value = "emp/controller/getEmployeeDetails/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> getEmployeeDetails(@PathVariable("id") int myId) {
		Optional<Employee> employee = employeeService.getEmployeeDetails(myId);

		if (employee.isPresent()) {
			return new ResponseEntity<Employee>(employee.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<Employee>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/emp/controller/createEmployee", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_HTML_VALUE)
	public ResponseEntity<String> createEmployee(@RequestBody Employee employee) {
		int count=employeeService.createEmployee(employee);
		return new ResponseEntity<String>("Employee added successfully with id:" + count,HttpStatus.CREATED);
	}

	@RequestMapping(value = "/emp/controller/updateEmployee", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Employee> updateEmployee(@RequestBody  Employee employee) {

		  Optional<Employee> employee2 = employeeService.getEmployeeDetails(employee.getEmployeeId());
		  if (!employee2.isPresent()) {
		  return new ResponseEntity<Employee>(employee2.get(),HttpStatus.INTERNAL_SERVER_ERROR);
		  }
		Employee employeeData = employee2.get();
		employeeData.setFirstName(employee.getFirstName());
		employeeData.setLastName(employee.getLastName());
		employeeData.setSalary(employee.getSalary());
		employeeData.setAge(employee.getAge());
		employeeData.setOrganisation(employee.getOrganisation());

		Employee updated = employeeService.updateEmployee(employeeData);
		return new ResponseEntity<Employee>(updated, HttpStatus.OK);
	}

	@RequestMapping(value = "/emp/controller/deleteEmployee/{id}", method = RequestMethod.DELETE,
	produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> deleteEmployee(@PathVariable("id") int myId) {
		if (!employeeService.getEmployeeDetails(myId).isPresent()) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		employeeService.deleteEmployee(myId);
		return new ResponseEntity<String>("Employee deleted successfully with id:" +myId, HttpStatus.OK);
	}
}
