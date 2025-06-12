package com.cesar.JwtServer;

import com.cesar.JwtServer.util.DatabaseDataPreLoadingUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JwtServerApplication {

    public static void main(String[] args) {
		SpringApplication.run(JwtServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {
			// Pre-load database data
			databaseDataPreLoadingUtils.initDefaultPermissionsAndRoles();
			databaseDataPreLoadingUtils.initTestUsers();
		};
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}


	public JwtServerApplication(DatabaseDataPreLoadingUtils databaseDataPreLoadingUtils) {
		this.databaseDataPreLoadingUtils = databaseDataPreLoadingUtils;
	}
	private final DatabaseDataPreLoadingUtils databaseDataPreLoadingUtils;
}