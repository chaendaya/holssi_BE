package org.example.holssi_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HolssiBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HolssiBeApplication.class, args);
	}

}
