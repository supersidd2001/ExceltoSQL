// src/main/java/com/my/qfc/service/code/MyServiceApplication.java
package com.my.qfc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = "com.my.qfc")
@AutoConfiguration
@EnableScheduling
public class MyServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyServiceApplication.class, args);
	}
}
