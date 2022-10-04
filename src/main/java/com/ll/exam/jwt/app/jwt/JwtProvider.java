package com.ll.exam.jwt.app.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    private final SecretKey jwtSecretKey;
    public SecretKey getSecretKeyNoCache(){
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

/*    @PostConstruct
    protected void init(){
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        jwtSecretKey= Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }*/
    private SecretKey getSecretKey(){
        return jwtSecretKey;
    }
    public SecretKey getSecretKeyPublic(){
        return jwtSecretKey;
    }
}
