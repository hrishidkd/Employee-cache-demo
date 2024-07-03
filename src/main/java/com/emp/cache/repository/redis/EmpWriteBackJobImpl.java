package com.emp.cache.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.emp.cache.config.EmpCacheProperties;
import com.emp.cache.entity.Employees;
import com.emp.cache.repository.EmployeeRepository;

@Service
public class EmpWriteBackJobImpl implements EmpWriteBackJob {

	private final EmpCacheProperties cacheProperties;
	private final EmployeeRepository empRepository;
	private final RedisTemplate<String, Employees> empRedisTemplate;

	public EmpWriteBackJobImpl(EmpCacheProperties cacheProperties, EmployeeRepository empRepository,
			RedisTemplate<String, Employees> empRedisTemplate) {
		this.cacheProperties = cacheProperties;
		this.empRepository = empRepository;
		this.empRedisTemplate = empRedisTemplate;
	}

	@Override
	@Scheduled(fixedRateString = "${emp-service.cache.write-back-rate}")
	public void writeBack() {
		// TODO Auto-generated method stub
		final var amountOfPeopleToPersist = empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).size();
		if (amountOfPeopleToPersist == null || amountOfPeopleToPersist == 0) {
			System.out.println("None people to write back from cache to database");
			return;
		}
		System.out.println("Found {" + amountOfPeopleToPersist + "} people to write back from cache to database");
		final var setOperations = empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey());
		final var scanOptions = ScanOptions.scanOptions().build();
//		try (final var cursor = setOperations.scan(scanOptions)) {
			final var cursor = setOperations.scan(scanOptions);
			assert cursor != null;
			while (cursor.hasNext()) {
				final var emp = cursor.next();
				empRedisTemplate.boundSetOps(cacheProperties.getWriteBackKey()).remove(emp);
				empRepository.save(emp);
				empRedisTemplate.boundValueOps(Integer.toString(emp.getId())).set(emp);
				System.out.println("Person saved (emp={" + emp + "})");
			
				System.out.println(
						"Person removed from {" + cacheProperties.getWriteBackKey() + "} set (emp={" + emp + "})");
			}
			System.out.println("Persisted {" + amountOfPeopleToPersist + "} people in the database");
//		} catch (RuntimeException exception) {
//			System.out.println("Error reading {" + cacheProperties.getWriteBackKey() + "} set from Redis"+
//					 exception);
//		}
	}

}
