package com.xuzhu.fmsincomemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FmsIncomemanagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmsIncomemanagementServiceApplication.class, args);
	}
}
