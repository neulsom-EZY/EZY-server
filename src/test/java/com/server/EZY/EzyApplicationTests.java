package com.server.EZY;

import com.server.EZY.model.user.UserEntity;
import com.server.EZY.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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


	@Autowired
	private UserRepository userRepository;

	@Test
	void 닉네임_존재_여부_검증() throws Exception{
		// Given
		UserEntity userEntity = new UserEntity();
		userEntity.setNickname("jihwan");

		// When
		UserEntity savedUser = userRepository.save(userEntity);
		System.out.println("======= saved ========");
		userRepository.flush();
		System.out.println("========== flushed ==========");

		// then
		boolean isExists = userRepository.existsByNickname("jihwan");
		System.out.println("isExists value is = "+isExists);
		assertThat(isExists).isTrue();
	}
}
