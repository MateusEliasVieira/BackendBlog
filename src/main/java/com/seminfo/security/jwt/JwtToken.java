package com.seminfo.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.seminfo.domain.model.User;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;


public class JwtToken {

	private static final String EMISSOR = "MATEUS@DEV";
	private static final String TOKEN_KEY = "01234567890123456789012345678901"; // Chave deve ter 256 bits, nesse caso 32 caracteres, para a criptografia
	private static final long MINUTOS = 60;

	public static String generateTokenJWT(User user)
	{
		try{
			String token = JWT.create()
					.withSubject(user.getUsername()) // (Payload) define para quem é esse token (Sujeito)
					.withIssuer(EMISSOR) // (Payload) minha referencia (Emissor)
					.withExpiresAt(LocalDateTime.now().plusMinutes(MINUTOS).toInstant(ZoneOffset.of("-03:00"))) // (Payload)
					.withClaim("idUser", user.getIdUser()) // (Payload) id do usuário
					.withClaim("permission", String.valueOf(user.getRole())) // Permissão do usuário
					.sign(Algorithm.HMAC256(TOKEN_KEY.getBytes())); // (Signature)

			return token;

		}catch(JWTCreationException jwtCreationException){
			throw new RuntimeException(jwtCreationException.getMessage());
		}

	}

	public static DecodedJWT decodeTokenJWT(String token){
		try{
			// A segurança do token, está na chave, apenas eu tenho
			DecodedJWT decode = JWT.require(Algorithm.HMAC256(TOKEN_KEY))
					.withIssuer(EMISSOR)
					.build()
					.verify(token);// verifica se é valido
			return decode;
		}catch(JWTVerificationException jwtVerificationException){
			throw new RuntimeException(jwtVerificationException.getMessage());
		}
	}

}
