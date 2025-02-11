package com.search.docsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@ComponentScan(basePackages =  {"com.search.*"})
@EnableConfigurationProperties
public class DocSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocSearchApplication.class, args);
	}

}
