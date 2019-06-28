package com.dosimple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DosimpleMessageApplication{

	public static void main(String[] args) {
		SpringApplication.run(DosimpleMessageApplication.class, args);
	}
}
