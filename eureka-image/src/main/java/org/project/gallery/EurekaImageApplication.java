package org.project.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
// Enabling Eureka client
@EnableEurekaClient
public class EurekaImageApp {

	public static void main(String[] args) {
		SpringApplication.run(EurekaImageApp.class, args);
	}

}
