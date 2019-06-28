package com.dosimple;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.dosimple.biz.user.config.DynamicDataSourceRegister;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@Import(DynamicDataSourceRegister.class)
@MapperScan("com.dosimple.biz.user.mapper")
@EnableDistributedTransaction
public class BizUserApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BizUserApplication.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}
}
