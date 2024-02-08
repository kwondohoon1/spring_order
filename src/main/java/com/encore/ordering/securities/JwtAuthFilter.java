package com.encore.ordering.securities;

import com.encore.ordering.common.ErrorResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthFilter extends GenericFilter {

    @Value("${jwt.secretKey}")
    private String secretKey;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String bearerToken = ((HttpServletRequest) request).getHeader("Authorization");
            if (bearerToken != null) {
                if (!bearerToken.substring(0, 7).equals("Bearer ")) {
                    throw new AuthenticationServiceException("token의 형식이 맞지않습니다.");
                }
                //            bearer토큰에서 토큰값만 추출
                String token = bearerToken.substring(7);
                //            "mysecret"
                //            추출된 토큰을 검증 후 Authentication객체 생성
                Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//                Authentication객체를 생성하기 위한 UserDetails 생성
                List<GrantedAuthority> authorities = new ArrayList<>();
//        ROLE_ 권한 이 패턴으로 스프링에서 기본적으로 권한체크
                authorities.add(new SimpleGrantedAuthority("ROLE_" + claims.get("role")));
                UserDetails userDetails = new User(claims.getSubject(), "", authorities);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
//            filterchain에서 그다음 filter로 넘어가도록 하는 메서드
            chain.doFilter(request, response);

        } catch (Exception e) {
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getWriter().write(ErrorResponseDto.makeMessage(HttpStatus.UNAUTHORIZED, e.getMessage()).toString());
        }
    }
}
