package com.encore.ordering.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private int expiration;


    public String createToken(String email, String role){
//        claims : 클레임은 토큰사용자에 대한 속성이나 데이터포함. 주로 페이 로드를 의미.
        Claims claims = Jwts.claims().setSubject(email);
        log.debug("expiration" + expiration);
        log.debug("secretKey" + secretKey);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + expiration*60*1000L))
        .signWith(SignatureAlgorithm.HS256, secretKey)
        .compact();
        return token;
    }
}
