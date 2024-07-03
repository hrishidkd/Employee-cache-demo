package com.emp.cache.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.emp.cache.entity.Employees;
import com.emp.cache.repository.EmployeeRepository;
import com.emp.cache.service.EmpService;

@RestController
@RequestMapping("/api")
public class EmployeeController {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmpService empService;
	
	@PostMapping("/saveEmployee")
	public String save() {
		Employees emp = new Employees();
		emp.setDepartment("Test1");
		emp.setName("test");
		
		empService.save(emp);
//			employeeRepository.save(emp);
		
		return "Status OK";
	}
	
//	@CachePut(cacheNames = "product", key = "#id")
	@PutMapping("/updateEmployee/{id}")
	public Employees updateEmployee(@PathVariable Long id, @RequestBody Employees emp) {
		Employees employee = employeeRepository.findById((long) emp.getId()).get();
		employee.setDepartment(emp.getDepartment());
		employee.setName(emp.getName());		
		
		return empService.updateEmployee(employee);
//		return employeeRepository.save(employee);
	}
	
//	@Cacheable(value = "product", key = "#id")
	@GetMapping("/getEmployeeById")
	public Employees getEmployeeById(int id) {
		
		return empService.getEmployee(id);
//		return employeeRepository.findById(id).get();
	}
	
	@GetMapping("/getEmployees")
	public List<Employees> getEmployees() {
		
		return employeeRepository.findAll();
	}
	
//	@CacheEvict(cacheNames = "product", key = "#id", beforeInvocation = true)
	@DeleteMapping("/deleteEmployees/{id}")
	public String deleteEmployees(@PathVariable Long id) {
		
		empService.deleteEmployee(id);
		employeeRepository.deleteById(id);
		
		return "Record deleted successfully.";
	}
}
