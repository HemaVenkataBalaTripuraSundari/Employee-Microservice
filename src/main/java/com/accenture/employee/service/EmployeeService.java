package com.accenture.employee.service;

import java.util.List;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.accenture.employee.dao.EmployeeRepository;
import com.accenture.employee.model.Employee;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repo;

	@Autowired
	private KafkaTemplate<String,String> kafka;

	@Value("${kafka.topic}")
	private String topic;

	public List<Employee> listEmployeeDetails(){
		return repo.findAll();
	}

	public Optional<Employee> getEmployeeDetails(int id){
		return repo.findById(id);
	}

	public Integer createEmployee(Employee employee){
		Employee created = repo.save(employee);
		return created.getEmployeeId();
	}

	public Employee updateEmployee(Employee employee){
		Employee updated = repo.save(employee);
		if (updated != null) {

			  JSONObject payrollJson = new JSONObject(); //
			  payrollJson.put("employeeId",employee.getEmployeeId()); //
			  payrollJson.put("salary",employee.getSalary());
			  kafka.send(topic, payrollJson.toString());
		}
		return updated;
	}

	public void deleteEmployee(Integer id){
		repo.deleteById(id);
	}

}
