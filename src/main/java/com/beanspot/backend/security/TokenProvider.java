package com.beanspot.backend.security;

import com.beanspot.backend.entity.User;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.token-validity-in-seconds}")
    private long validityInSeconds;

    @Value("${jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenValidityInSeconds;

    @Value("${jwt.allowed-clock-skew-seconds}")
    private long allowedClockSkewSeconds;

    private JwtParser parser;

    @PostConstruct
    void init(){
        this.parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .setAllowedClockSkewSeconds(allowedClockSkewSeconds)
                .requireIssuer(issuer)
                .build();
    }

    public String createAccessToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusSeconds(validityInSeconds));


        return Jwts.builder()
                .signWith( SignatureAlgorithm.HS512, secretKey)
                .setIssuer("beanspot")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate) // exp
                .setSubject(user.getId().toString())
                .claim("userId", user.getUserId())
                .claim("name", user.getNickname())
                .claim("role", user.getRole().name()) // 권한 정보 추가
                .claim("type", "access")
                .compact();
    }

    public String createTemporaryAccessToken(String socialType, String socialId) {
        Date expiryDate = Date.from(Instant.now().plusSeconds(validityInSeconds));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .setIssuer(issuer)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .setSubject("temporary")
                .claim("socialId", socialId)
                .claim("socialType", socialType)
                .claim("isProfileComplete", false)
                .claim("type", "temporary")
                .compact();
    }

    public String createRefreshToken(User user) {
        Date expiryDate = Date.from(Instant.now().plusSeconds(validityInSeconds));
        return Jwts.builder()
                .signWith( SignatureAlgorithm.HS512, secretKey)
                .setIssuer("beanspot")
                .setIssuedAt(new Date())
                .setExpiration(expiryDate) // exp
                .setSubject(user.getId().toString())
                .claim("type", "refresh")
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = getAllClaims(token);
        return Long.parseLong(claims.getSubject());

    }

    public String getTokenType(String token) {
        Claims claims = getAllClaims(token);
        Object t = claims.get("type");
        return t != null ? t.toString() : null;
    }

    public String getSocialIdFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("socialId", String.class);
    }

    public String getSocialTypeFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("socialType", String.class);
    }

    public String getRoleFromToken(String token) {
        Claims claims = getAllClaims(token);
        return claims.get("role", String.class);
    }

    private Claims getAllClaims(String token) {
        return parser.parseClaimsJws(token).getBody();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰 없거나 잘못되었습니다.");
        }
        return false;
    }

    /**
     * 채팅 : JWT 토큰 정보를 기반으로 Spring Security 인증 객체 생성
     * StompHandler에서 웹소켓 연결 시 유저를 식별하기 위해 필요
     * @param token 검증이 완료된 JWT AccessToken
     * @return Spring Security에서 사용하는 유저 인증 객체 (Authentication)
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getAllClaims(token);
        String principal = claims.getSubject();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");

        return new UsernamePasswordAuthenticationToken(principal, token, Collections.singleton(authority));
    }

}
