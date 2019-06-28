package com.dosimple;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import com.dosimple.common.constant.IdBizTags;
import com.dosimple.biz.shop.constant.Constant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.LinkedBlockingDeque;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableTransactionManagement
@MapperScan("com.dosimple.biz.shop.mapper")
@EnableDistributedTransaction
public class BizShopApplication {

	public static void main(String[] args) {
		for (IdBizTags idBizTags : IdBizTags.values()) {
			Constant.CACHE.put(idBizTags.name(), new LinkedBlockingDeque<>());
		}
		SpringApplication.run(BizShopApplication.class, args);
	}
}
