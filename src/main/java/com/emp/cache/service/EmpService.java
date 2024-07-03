package com.emp.cache.service;

import java.util.Random;
import java.util.UUID;
import java.util.random.RandomGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.emp.cache.config.EmpCacheProperties;
import com.emp.cache.entity.Employees;
import com.emp.cache.repository.EmployeeRepository;

@Service
public class EmpService {

	private EmpCacheProperties cacheProperties;
	private final EmployeeRepository empRepository;

	@Autowired
	private final RedisTemplate<String, Employees> empRedisTemplate;

	public EmpService(EmployeeRepository empRepository,
			RedisTemplate<String, Employees> empRedisTemplate,EmpCacheProperties cacheProperties) {
		this.empRepository = empRepository;
		this.empRedisTemplate = empRedisTemplate;
		this.cacheProperties = cacheProperties;
	}

	public Employees save(Employees emp) {

		Employees emp1 = new Employees();
		Random rand = new Random();
//		emp1.setId(1);
		emp1.setDepartment(Integer.toString(rand.nextInt()));
		emp1.setName(emp.getName());
		UUID uuid = UUID.randomUUID();
//		emp1.setId(1);
//		empRedisTemplate.boundValueOps(Integer.toString(emp1.getId())).set(emp1);
		empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).add(emp1);
		System.out.println("Person cached (key={" + emp1.getId() + "}, value={" + emp1 + "})");// ("Person cached (key={},
																								// value={})",
																								// emp.getId(), emp);

		return emp1;

//		return empRepository.save(emp);
	}

	public Employees getEmployee(int id) {

		final var empOnCache = empRedisTemplate.boundValueOps(Integer.toString(id)).get();
		if (empOnCache != null) {
			System.out.println("Person retrieved from cache (personId={" + id + "})");
			return empOnCache;
		}
		final var empNotCached = empRepository.findById((long) id);
		if (empNotCached.isPresent()) {
			System.out.println("Person retrieved from database (personId={" + id + "})");

			final var emp = empNotCached.get();
			empRedisTemplate.boundValueOps(Integer.toString(emp.getId())).set(emp);
//			empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).add(emp);
			System.out.println("Person cached (key={"+id+"}, value={"+emp+"})");

			return emp;
		}
		return null;
	}
	
	public Employees updateEmployee(Employees emp) {

		final var empOnCache = empRedisTemplate.boundValueOps(Integer.toString(emp.getId())).get();
		if (empOnCache != null) {
			System.out.println("Person retrieved from cache (personId={" + emp.getId() + "})");
			empRedisTemplate.boundValueOps(Integer.toString(emp.getId())).set(emp);
			empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).add(emp);
			return empOnCache;
		}
		final var empNotCached = empRepository.findById((long) emp.getId());
		if (empNotCached.isPresent()) {
			System.out.println("Person retrieved from database (personId={" + emp.getId() + "})");

			final var emp1 = empNotCached.get();
			empRedisTemplate.boundValueOps(Integer.toString(emp1.getId())).set(emp1);
			empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).add(emp1);
			System.out.println("Person cached (key={"+emp.getId()+"}, value={"+emp1+"})");

			return emp1;
		}
		return null;
	}
	
	public Employees deleteEmployee(Long id) {

		final var empOnCache = empRedisTemplate.boundValueOps(Long.toString(id)).get();
		if (empOnCache != null) {
			System.out.println("Person retrieved from cache (personId={" + id + "})");
			empRedisTemplate.boundValueOps(Long.toString(id)).getAndDelete();
			empRepository.deleteById(id);
			return empOnCache;
		}
		final var empNotCached = empRepository.findById(id);
		if (empNotCached.isPresent()) {
			System.out.println("Person retrieved from database (personId={" + id + "})");

			final var emp1 = empNotCached.get();
//			empRedisTemplate.boundValueOps(Integer.toString(emp1.getId())).set(emp1);
//			empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).add(emp1);
			System.out.println("Person cached (key={"+id+"}, value={"+emp1+"})");

			return emp1;
		}
		return null;
	}
}
