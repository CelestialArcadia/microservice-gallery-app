package com.eureka.zuul.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.eureka.common.security.JwtConfig;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private final JwtConfig jwtConfig;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// Getting tokens from the authentication header, as supposed to be
		String header = request.getHeader(jwtConfig.getHeader());

		// Validation of the header and the prefix
		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			filterChain.doFilter(request, response);
		}

		// In case there was no token provided, hence the user won't be authenticated.
		// Cause: Maybe the user accessing a public path or asking for a token.

		// All secured paths that needs a token are already defined and secured in
		// configuration class.
		// And If user tried to access without access token, then he won't be
		// authenticated and an exception will be thrown.

		// Getting the token
		String token = header.replace(jwtConfig.getPrefix(), "");

		// exceptions might be thrown in creating the claims if for example the token is
		// expired
		try {

			// Token validation
			Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
					.getBody();
			String username = claims.getSubject();

			if (username != null) {

				List<String> authorities = (List<String>) claims.get("authorities");

				// Creation of the authentication object
				// UsernamePasswordAuthenticationToken: A built-in object, used by spring to
				// represent the current authenticated / being authenticated user.
				// It needs a list of authorities, which has type of GrantedAuthority interface,
				// where SimpleGrantedAuthority is an implementation of that interface
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));

				// Authenticating the user
				SecurityContextHolder.getContext().setAuthentication(auth);

			}

		} catch (Exception e) {
			SecurityContextHolder.clearContext();
		}

		// next filter in the filter chain
		filterChain.doFilter(request, response);
	}

}