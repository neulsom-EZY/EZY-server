package com.server.EZY;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

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
		assertThat(jwtKey.equals(expectValue));
	}

}
