package com.emp.cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.emp.cache.config.EmpCacheProperties;

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
@EnableConfigurationProperties(EmpCacheProperties.class)
public class EmployeeCacheDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeCacheDemoApplication.class, args);
	}

}
