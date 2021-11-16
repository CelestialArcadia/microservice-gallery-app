package com.eureka.zuul.security;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.eureka.common.security.JwtConfig;

// Enabling security configuration. 
// This annotation denotes configuration for spring security.
@EnableWebSecurity
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtConfig jwtConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
				// session won't be used to store user's state, thus we make it stateless
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				// handle an authorized attempts
				.exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				// A filter added to validate the tokens with every request
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
				// authorization requests configuration
				.authorizeRequests()
				// allow all who are accessing "authentication" service
				.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
				// must be an admin if trying to access admin area (authentication is also
				// required here)
				.antMatchers("/gallery" + "/admin/**").hasRole("ADMIN")
				// Any other request must be authenticated
				.anyRequest().authenticated();
	}

	@Bean
	public JwtConfig jwtConfig() {
		return new JwtConfig();
	}

}
