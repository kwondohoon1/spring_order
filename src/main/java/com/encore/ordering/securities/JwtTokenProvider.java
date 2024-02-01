package com.encore.ordering.securities;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class JwtTokenProvider {
    public String createToken(String email, String role){
//        claims : 클레임은 토큰사용자에 대한 속성이나 데이터포함. 주로 페이 로드를 의미.
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        Date now = new Date();
        String token = Jwts.builder()
        .setClaims(claims)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + 30*60*1000L))
        .signWith(SignatureAlgorithm.HS256, "mysecret")
        .compact();
        return token;
    }
}
