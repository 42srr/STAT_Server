package ggs.srr.security.jwt;

import ggs.srr.security.jwt.request.CreateJwtRequest;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_expire_ms}")
    private Long accessExpireMs;

    @Value("${jwt.refresh_expire_ms}")
    private Long refreshExpireMs;

    public JwtTokenResponse createJwtToken(CreateJwtRequest request) {

        String accessToken = createAccessToken(request, true);
        String refreshToken = createAccessToken(request, false);

        return new JwtTokenResponse(accessToken, refreshToken, accessExpireMs / 1000);
    }

    private String createAccessToken(CreateJwtRequest request, boolean isAccessToken) {
        long nowMs = System.currentTimeMillis();
        long expireMs = isAccessToken ? accessExpireMs : refreshExpireMs;

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .issuedAt(new Date(nowMs))
                .claim("intraId", request.getIntraId())
                .claim("role", request.getRole().getText())
                .expiration(new Date(nowMs + expireMs))
                .signWith(secretKey)
                .compact();
    }

}
