package com.atguigu.gmall.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.atguigu.gmall")
@SpringBootApplication
public class GmallPassportWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(GmallPassportWebApplication.class, args);
	}
}
