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
public class WebFilterConfiguration {

	/*
	 * @Bean é usada para indicar que um método específico em uma classe é
	 * responsável por criar e configurar um objeto que será gerenciado pelo
	 * contêiner do Spring.
	 * 
	 * Ao usar a anotação @Bean, o método anotado é invocado durante a fase de
	 * inicialização do contêiner do Spring, e o objeto retornado por esse método é
	 * registrado como um bean no contexto do Spring. Isso permite que o contêiner
	 * do Spring gerencie o ciclo de vida do objeto, além de fornecer recursos
	 * adicionais, como injeção de dependência, transações e controle de transação,
	 * entre outros.
	 * 
	 * Resumo: Criamos um objeto para o spring utilizar
	 */

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
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
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http.cors();//cors ativado
		
		http.csrf((csrf) -> csrf.disable());

		http.authorizeHttpRequests((auth) -> auth
				.requestMatchers(HttpMethod.GET, "/email/confirmation/*").permitAll()
				.requestMatchers(HttpMethod.POST, "/login/enter").permitAll()
				.requestMatchers(HttpMethod.POST, "/login/google").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/new").permitAll()
				.anyRequest().authenticated());

		// A classe UsernamePasswordAuthenticationFilter vai obter a authenticação (caso
		// exista) do contexto de segurança do spring security
		// e então verificará as roles deste usuario e demais informações passadas na
		// autenticação para o contexto do spring security, como definimos nessa classe
		// UsernamePasswordAuthenticationToken auth = new
		// UsernamePasswordAuthenticationToken("user", null, Collections.emptyList());
		http.addFilterBefore(new FiltroInterceptador(), UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
}
