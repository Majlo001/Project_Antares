package com.majlo.antares;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectAntaresApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectAntaresApplication.class, args);
	}

}
