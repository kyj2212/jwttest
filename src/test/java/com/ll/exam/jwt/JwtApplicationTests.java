package com.ll.exam.jwt;

import com.ll.exam.jwt.jwt.JwtProvider;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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
	@DisplayName("secretkey 객체가 @PostContruct에 의해 싱글톤으로 생성된다.")
	void t3(){
		//SecretKey secretKey1 = jwtProvider.getSecretKey();
		//SecretKey secretKey2 = jwtProvider.getSecretKey();
		SecretKey secretKey1 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		SecretKey secretKey2 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		System.out.println("secretkey1 equals secretkey2 : "+secretKey1.equals(secretKey2));
		System.out.println("secretkey1 == secretkey2 : "+ (secretKey1==secretKey2) );
		System.out.println("secretkey1 : "+System.identityHashCode(secretKey1));
		System.out.println("secretkey2 : "+System.identityHashCode(secretKey2));
		assertThat(secretKey1==secretKey2).isTrue();
	}

	@Test
	@DisplayName("secretkey 객체를 그냥 단순히 private로 생성해서 외부에서 강제로 호출")
	void t4(){
		SecretKey secretKey1 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		SecretKey secretKey2 = TestUtil.callMethod(jwtProvider, "getSecretKey");
		System.out.println("secretkey1 equals secretkey2 : "+secretKey1.equals(secretKey2));
		System.out.println("secretkey1 == secretkey2 : "+ (secretKey1==secretKey2) );
		System.out.println("secretkey1 : "+System.identityHashCode(secretKey1));
		System.out.println("secretkey2 : "+System.identityHashCode(secretKey2));
		assertThat(secretKey1==secretKey2).isTrue();
	}

	@Test
	@DisplayName("accessToken 을 얻는다.")
	void t5() {

		// 회원번호가 1이고
		// username이 admin 이고
		// ADMIN 역할과 MEMBER 역할을 동시에 가지고 있는 회원정보를 구성
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");
		claims.put("authorities", Arrays.asList(
				new SimpleGrantedAuthority("ADMIN"),
				new SimpleGrantedAuthority("MEMBER"))
		);
		// 구성 끝

		// 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
		String accessToken = jwtProvider.generateAccessToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(accessToken).isNotNull();
	}
	@Test
	@DisplayName("accessToken 을 통해서 claims 를 얻을 수 있다.")
	void t6() {
		// 회원번호가 1이고
		// username이 admin 이고
		// ADMIN 역할과 MEMBER 역할을 동시에 가지고 있는 회원정보를 구성
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", 1L);
		claims.put("username", "admin");
		claims.put("authorities", Arrays.asList(
				new SimpleGrantedAuthority("ADMIN"),
				new SimpleGrantedAuthority("MEMBER"))
		);
		// 구성 끝
		System.out.println("claims : " + claims);
		// 지금으로부터 5시간의 유효기간을 가지는 토큰을 생성
		String accessToken = jwtProvider.generateAccessToken(claims, 60 * 60 * 5);

		System.out.println("accessToken : " + accessToken);

		assertThat(jwtProvider.verify(accessToken)).isTrue();

		Map<String, Object> claimsFromToken = jwtProvider.getClaims(accessToken);
		System.out.println("claimsFromToken : " + claimsFromToken);
	}
}
