package com.dosimple;

import com.dosimple.task.config.elasticjob.annotation.EnableElasticJob;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableElasticJob
@EnableEurekaClient
public class DosimpleTaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosimpleTaskApplication.class, args);
	}
}
