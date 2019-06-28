package com.dosimple.dosimplemonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class DosimpleMonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosimpleMonitorApplication.class, args);
	}

}
