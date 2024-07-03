package com.emp.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "emp-service.cache")
@Getter
@Setter
public class EmpCacheProperties {
	private long writeBackRate;

	private String writeBackKey;
}
