package com.rroager.walletnamingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class WalletNamingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletNamingServerApplication.class, args);
	}

}
