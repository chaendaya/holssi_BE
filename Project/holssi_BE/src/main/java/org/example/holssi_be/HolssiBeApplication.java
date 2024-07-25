package org.example.holssi_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "org.example.holssi_be.repository")
@EntityScan(basePackages = "org.example.holssi_be.entity.domain")
@EnableScheduling
public class HolssiBeApplication {
	public static void main(String[] args) {
		SpringApplication.run(HolssiBeApplication.class, args);
	}

}
