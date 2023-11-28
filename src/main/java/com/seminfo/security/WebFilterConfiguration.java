package com.seminfo.security;

import com.seminfo.domain.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebFilterConfiguration {

	@Autowired
	private InterceptorFilter interceptorFilter;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.addAllowedOrigin("*"); // Permite todas as origens, você pode personalizar isso
		// Methods http authorized
		configuration.addAllowedMethod("GET");
		configuration.addAllowedMethod("POST");
		configuration.addAllowedMethod("PUT");
		configuration.addAllowedMethod("DELETE");
		configuration.addAllowedMethod("OPTIONS");
		// Headers http authorized
		configuration.addAllowedHeader("Authorization");
		configuration.addAllowedHeader("Content-Type");

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
	{
		http.cors(); // cross origin resource sharing (compartilhamento de recursos de origens cruzadas)

		http.csrf(AbstractHttpConfigurer::disable); // Habilita a segurança contra ataques csrf (Cross-site request forgery)

		http.formLogin(AbstractHttpConfigurer::disable); // Desabilita formulários de login html

		http.httpBasic(AbstractHttpConfigurer::disable);

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sem sessões

		http.authorizeHttpRequests((auth) -> auth
				// Login Controller
				.requestMatchers(HttpMethod.POST, "/login/*").permitAll()
				// Email Controller
				.requestMatchers(HttpMethod.GET, "/email/confirmation/*").permitAll()
				// Recover Account Controller
				.requestMatchers(HttpMethod.GET, "/recover/recover-account/*").permitAll()
				.requestMatchers(HttpMethod.POST, "/recover/new-password").hasAuthority(Roles.ROLE_USER.name())
				// User Controller
				.requestMatchers(HttpMethod.POST, "/user/new").permitAll()
				.requestMatchers(HttpMethod.GET, "/user/find/*").hasAuthority(Roles.ROLE_USER.name())
				// Post Controller
				.requestMatchers(HttpMethod.POST, "/posts/new").hasAuthority(Roles.ROLE_ADMIN.name()) // Apenas usuários administradores podem postar
				.requestMatchers(HttpMethod.GET, "/posts/all").hasAuthority(Roles.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/posts/page/*").hasAuthority(Roles.ROLE_USER.name())
				.requestMatchers(HttpMethod.GET, "/posts/read-post/*").hasAuthority(Roles.ROLE_USER.name())
				.anyRequest().authenticated());

		http.addFilterBefore(this.interceptorFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}


}
