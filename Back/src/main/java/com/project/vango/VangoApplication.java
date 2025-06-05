package com.project.vango;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VangoApplication {

	public static void main(String[] args) {
		SpringApplication.run(VangoApplication.class, args);
	}

}
