package com.ll.exam.jwt.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ll.exam.jwt.app.security.entity.MemberContext;
import com.ll.exam.jwt.util.Util;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtProvider {

    @Value("${custom.jwt.secretKey}")
    private String secretKeyPlain;

    private SecretKey jwtSecretKey;
    private final UserDetailsService userDetailsService;
    @PostConstruct
    private void init(){
        System.out.println("[PostConstruct] init Jwt secret key");
        String keyBase64Encoded = Base64.getEncoder().encodeToString(secretKeyPlain.getBytes());
        jwtSecretKey= Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }
    private SecretKey getSecretKey(){
        return jwtSecretKey;
    }

    public String generateAccessToken(Map<String, Object> claims, int seconds) {
        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Map<String,Object> claims = getClaims(token);
        log.debug("claims : "+ claims);
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("authorities").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        // UserDetails ????????? ???????????? Authentication ??????

        User principal = new User(claims.get("username").toString(),"",authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);

    }

    private Claims parseClaimsJws(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(jwtSecretKey).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }
    public Claims getClaimsC(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return (Claims) Util.json.toMap(body);
    }
    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Util.json.toMap(body);
    }

}
