package com.ll.exam.jwt;

import com.ll.exam.jwt.app.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.SecretKey;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.spliterator;

@SpringBootTest
class JwtApplicationTests {

	@Value("${custom.jwt.secretKey}")
	private String secretKeyPlain;

	@Autowired
	private JwtProvider jwtProvider;

	@Test
	void contextLoads() {
	}
	@Test
	@DisplayName("SecretKey가 존재해야 한다.")
	void t1() {
		assertThat(secretKeyPlain).isNotNull();
	}
	@Test
	@DisplayName("SecretKey 원문으로 SecretKey 객체 만들수 있다.")
	void t2() {
		String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
		System.out.println("평문 : "+secretKeyPlain);
		System.out.println("인코딩된 키 객체 : "+secretKey);
		System.out.println("인코딩된 키 알고리즘 : "+secretKey.getAlgorithm());
		assertThat(secretKey).isNotNull();
	}

	@Test
	@DisplayName("JwtProvider 객체로 SecretKey 객체 만들수 있다.")
	void t3() {
		SecretKey secretKey = jwtProvider.getSecretKeyPublic();
		System.out.println("인코딩된 키 객체 : "+secretKey);
		System.out.println("인코딩된 키 알고리즘 : "+secretKey.getAlgorithm());
		assertThat(secretKey).isNotNull();
	}
	@Test
	@DisplayName("secretkey 객체가 싱글톤으로 생성된다.")
	void t4(){
		SecretKey secretKey1 = jwtProvider.getSecretKeyPublic();
		SecretKey secretKey2 = jwtProvider.getSecretKeyPublic();
		System.out.println("secretkey1 equals secretkey2 : "+secretKey1.equals(secretKey2));
		System.out.println("secretkey1 == secretkey2 : "+ (secretKey1==secretKey2) );
		System.out.println("secretkey1 : "+System.identityHashCode(secretKey1));
		System.out.println("secretkey2 : "+System.identityHashCode(secretKey2));
		assertThat(secretKey1==secretKey2).isTrue();
	}
	@Test
	@DisplayName("secretkey 객체를 그냥 단순히 private로 생성해서 외부에서 강제로 호출")
	void t5(){
		SecretKey secretKey1 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		SecretKey secretKey2 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		System.out.println("secretkey1 equals secretkey2 : "+secretKey1.equals(secretKey2));
		System.out.println("secretkey1 == secretkey2 : "+ (secretKey1==secretKey2) );
		System.out.println("secretkey1 : "+System.identityHashCode(secretKey1));
		System.out.println("secretkey2 : "+System.identityHashCode(secretKey2));
		assertThat(secretKey1==secretKey2).isTrue();
	}
	@Test
	@DisplayName("secretkey 객체가 여러개 생성된다.")
	void t6(){
		SecretKey secretKey1 = jwtProvider.getSecretKeyNoCache();
		SecretKey secretKey2 = jwtProvider.getSecretKeyNoCache();
		System.out.println("secretkey1 equals secretkey2 : "+secretKey1.equals(secretKey2));
		System.out.println("secretkey1 == secretkey2 : "+ (secretKey1==secretKey2) );

		System.out.println("secretkey1 : "+System.identityHashCode(secretKey1));
		System.out.println("secretkey2 : "+System.identityHashCode(secretKey2));

		assertThat(secretKey1==secretKey2).isFalse();
	}
}
