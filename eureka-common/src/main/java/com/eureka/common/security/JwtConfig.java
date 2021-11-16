package com.eureka.common.security;

import org.springframework.beans.factory.annotation.Value;

import lombok.Getter;
import lombok.ToString;

// Getters will be created automatically by Lombok
@Getter
@ToString
public class JwtConfig {

	// Spring doesn't inject/autowire to "static" fields.
	// Link: https://stackoverflow.com/a/6897406

	@Value("${security.jwt.uri:/auth/**}")
	private String uri;

	@Value("${security.jwt.header:Authorization}")
	private String header;

	@Value("${security.jwt.prefix:Bearer }")
	private String prefix;

	@Value("${security.jwt.expiration:#{24*60*60}}")
	private int expiration;

	@Value("${security.jwt.secret:JwtSecretKey}")
	private String secret;
}
