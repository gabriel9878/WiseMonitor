package br.com.WiseMonitor.servico;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.WiseMonitor.modelos.User;

@Service
public class TokenService {

	@Value("${api.security.token.secret}")
	public String secret;

	
	public String generateToken(User u) {

		try {

			Algorithm algorithm = Algorithm.HMAC256(this.secret);
			
			//Retorna token gerado para o User u
			return JWT.create().withIssuer("wise-monitor").withSubject(u.getLogin())
					.withExpiresAt(generateExpirationDate()).sign(algorithm);


		} catch (JWTCreationException exception) {

			throw new RuntimeException("Erro ao gerar token", exception);

		}

	}

	public String validateToken(String token) {

		try {

			Algorithm algorithm = Algorithm.HMAC256(this.secret);
			return JWT.require(algorithm).withIssuer("wise-monitor").build().verify(token).getSubject();

		} catch (JWTVerificationException exception) {

			return ""; 
			
		}

	}


	private static Instant generateExpirationDate() {

		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));

	}

}
