package com.ll.exam.jwt.app.jwt;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;

@Component
public class JwtProvider {

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    private SecretKey secretKey;

    @PostConstruct
    protected void init(){
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        secretKey= Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    public SecretKey getSecretKey(){
        return secretKey;
    }
}
