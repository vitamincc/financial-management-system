package com.xuzhu.fmsexpensemanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class FmsExpensemanagementServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FmsExpensemanagementServiceApplication.class, args);
	}
}
