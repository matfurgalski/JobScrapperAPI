package com.matfurgalski.JobScrapper;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class JobScrapperApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JobScrapperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


	}
}
