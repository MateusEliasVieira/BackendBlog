package com.seminfo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebFilterConfiguration
{

	/* Um bean, no contexto do Spring Framework, é simplesmente um objeto gerenciado pelo contêiner Spring. */

	@Bean
	public CorsConfigurationSource corsConfigurationSource()
	{
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // Permite todas as origens, você pode personalizar isso
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("OPTIONS");
		configuration.addAllowedHeader("Authorization");
		configuration.addAllowedHeader("Content-Type");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.cors();
		http.csrf((csrf) -> csrf.disable());

		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers(HttpMethod.GET, "/email/confirmation/*").permitAll()
				.requestMatchers(HttpMethod.GET, "/email/recover-account/*").permitAll()
				.requestMatchers(HttpMethod.POST, "/login/enter").permitAll()
				.requestMatchers(HttpMethod.POST, "/login/google").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/new").permitAll()
				.anyRequest().authenticated());

		http.addFilterBefore(new FiltroInterceptador(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
