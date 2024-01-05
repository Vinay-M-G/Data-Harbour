package com.dataHarbour.engine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class DataHarbourApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataHarbourApplication.class, args);
	}

}
