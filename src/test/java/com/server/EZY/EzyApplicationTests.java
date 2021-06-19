package com.server.EZY;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EzyApplicationTests {

	@Value("${security.jwt.token.secret-key}")
	private String jwtKey;

	@Test
	void JWT_SECRET_KEY_검증() {
		// Given
		String expectValue = "afsdasfdASAWRSasgdvFTUJARGtasdfSRTJSRTEQR14asdfVA";

		// Then
		System.out.println("기대하는 값은= "+expectValue);
		System.out.println("============================");
		assertThat(jwtKey.equals(expectValue));
	}

	@PostConstruct
	protected String encodeInit(){
		return jwtKey = Base64.getEncoder().encodeToString(jwtKey.getBytes());
	}

	@Test
	void SECRET_KEY_ENCODE_검증(){
		System.out.println("encode 결과 값은: "+encodeInit());
	}
}
