package com.my.qfc.service.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MyServiceApplication {

	public static void main(String[] args) {
		try {
			SpringApplication.run(MyServiceApplication.class, args);
			System.out.println("Staart");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
