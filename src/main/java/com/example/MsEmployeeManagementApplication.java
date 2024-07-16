package com.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.example.repository.mapper")
public class MsEmployeeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsEmployeeManagementApplication.class, args);
	}

}
