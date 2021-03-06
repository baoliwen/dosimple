package com.dosimple;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableTransactionManagerServer
public class DosimpleTxManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(DosimpleTxManagerApplication.class, args);
	}

}
