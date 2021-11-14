package org.project.gallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableEurekaClient
public class EurekaGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaGalleryApplication.class, args);
	}

}

@Configuration
class RestTemplateConfig {

	// Creation of a bean for restTemplate to call services
	@Bean
	// Loading balance between instances running at different ports. Further
	// explanation in the ReadMe
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
