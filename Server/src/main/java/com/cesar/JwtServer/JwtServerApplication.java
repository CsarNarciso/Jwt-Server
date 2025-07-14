package com.cesar.JwtServer;

import com.cesar.JwtServer.util.AuthorityUtils;
import com.cesar.JwtServer.util.UserUtils;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class JwtServerApplication {

	private final UserUtils userUtils;
	private final AuthorityUtils authorityUtils;

	public JwtServerApplication(UserUtils userUtils, AuthorityUtils authorityUtils) {
		this.userUtils = userUtils;
		this.authorityUtils = authorityUtils;
	}

    public static void main(String[] args) {
		SpringApplication.run(JwtServerApplication.class, args);
	}

	@Bean
	public CommandLineRunner init() {
		return args -> {

			// Pre-load database data
			authorityUtils.initDefaultPermissionsAndRoles();
			userUtils.initDefaultAdmins();

			//Optional, preload test user (for dev testing)
			userUtils.initTestUser();
		};
	}

	@Bean
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
}