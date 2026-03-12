package com.consensus.consensus;

import com.consensus.consensus.auth.infrastructure.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ConsensusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConsensusApplication.class, args);
	}

}
