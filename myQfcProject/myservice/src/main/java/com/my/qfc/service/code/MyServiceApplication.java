package com.my.qfc.service.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.my.qfc.common.util.ExcelReaderStandalone;
import com.my.qfc.service.scheduler.MyScheduler;
import com.my.qfc.service.util.ApplicationProperties;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.my.qfc.common.util", "com.my.qfc.service" })
public class MyServiceApplication {
	ApplicationProperties applicationProperties;
	ExcelReaderStandalone excelReaderStandalone;
	MyScheduler myScheduler = new MyScheduler(applicationProperties, excelReaderStandalone);

	public static void main(String[] args) {
		SpringApplication.run(MyServiceApplication.class, args);
	}

}
