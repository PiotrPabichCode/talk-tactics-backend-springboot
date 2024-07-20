package com.piotrpabich.talktactics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TalkTacticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(TalkTacticsApplication.class, args);
	}

}
