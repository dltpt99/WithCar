package anu.ice.WithCar.security;

import anu.ice.WithCar.service.SignService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String salt;
    private Key secretKey;
    private final SignService userDetailsService;

    public JwtTokenProvider(@Lazy SignService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }

    //Jwt 토큰 생성
    public String createToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", role);
        Date now = new Date();
        // 10시간
        long TOKEN_VALID_MILISECOND = 1000L * 60 * 60 * 10;
        return Jwts.builder()
                .setClaims(claims) //데이터
                .setIssuedAt(now)  //토큰 발행일
                .setExpiration(new Date(now.getTime() + TOKEN_VALID_MILISECOND)) // 만료 기간
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    //인증 성공시 SecurityContextHolder에 저장할 Authentication 객체 생성
    //권한 정보 획득
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    public boolean validateToken(String token) {
        try {
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secretKey).build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }
}
