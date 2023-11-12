package com.seminfo.security;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import com.seminfo.domain.model.User;
import com.seminfo.domain.service.UserService;
import com.seminfo.domain.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.http.HttpServletRequest;


public class TokenUtil {

	private static final String EMISSOR = "seminfo2023";
	private static final String TOKEN_KEY = "01234567890123456789012345678901"; // Chave deve ter 256 bits, nesse caso		// 32 caracteres, para a criptografia
	private static final long MINUTOS = 1;

	public static String getToken(User user)
	{
		String token = JWT.create()
				.withSubject(user.getUsername()) // (Payload) define para quem é esse token (Sujeito)
				.withIssuer(EMISSOR) // (Payload) minha referencia (Emissor)
				.withExpiresAt(LocalDateTime.now().plusMinutes(MINUTOS).toInstant(ZoneOffset.of("-03:00"))) // (Payload)
				.withClaim("idUser", user.getIdUser()) // (Payload) id do usuário
				.withClaim("permission", String.valueOf(user.getPermission())) // Permissão do usuário
				.sign(Algorithm.HMAC256(TOKEN_KEY.getBytes())); // (Signature)

		return token;
	}

	public static Authentication getAuthentication(HttpServletRequest request) throws Exception
	{
		String token = request.getHeader("Authorization");

		if (token != null && !token.isEmpty())
		{
			token = token.replace("Bearer ", "");

			DecodedJWT decode = decodeTokenJWT(token);

			if (decode != null)
			{
				// token verificado e descriptografado
				String emissor = decode.getIssuer();
				Date validade = decode.getExpiresAt();
				String username_subject = decode.getSubject();
				//Long idUser = Long.parseLong(String.valueOf(decode.getClaim("idUser")));
				//String permission = String.valueOf(decode.getClaim("permission"));
				//String password = new UserServiceImpl().findUser(idUser).getPassword();

				if (emissor.equals(EMISSOR) && validade.after(new Date(System.currentTimeMillis())))
				{
					// caso a requisição tenha o cabeçalho correto, gero um "token interno"
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username_subject, null, Collections.emptyList());
					return authenticationToken;
				}
				
			}
			else
			{
				return null;
			}

		}
		else
		{
			return null;
		}

		return null;
	}

	public static DecodedJWT decodeTokenJWT(String token){
		// A segurança do token, está na chave, apenas eu tenho
		DecodedJWT decode = JWT.require(Algorithm.HMAC256(TOKEN_KEY))
				.withIssuer(EMISSOR)
				.build()
				.verify(token);// verifica se é valido
		return decode;
	}


}
