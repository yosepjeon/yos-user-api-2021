package com.yosep.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class YosUserApi2021Application {

	public static void main(String[] args) {
		SpringApplication.run(YosUserApi2021Application.class, args);
	}

}
