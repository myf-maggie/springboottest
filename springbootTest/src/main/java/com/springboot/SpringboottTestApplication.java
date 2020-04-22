package com.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@ComponentScan(value = "com.*")
@ComponentScan(basePackages = "com.util")
@Configuration
@EnableAutoConfiguration
public class SpringboottTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringboottTestApplication.class, args);
	}

}
