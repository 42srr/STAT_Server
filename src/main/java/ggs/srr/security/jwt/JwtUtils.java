package ggs.srr.security.jwt;

import ggs.srr.exception.security.authentication.AuthenticationException;
import ggs.srr.security.jwt.request.CreateJwtRequest;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

import static ggs.srr.exception.security.authentication.ErrorCode.EXPIRED_JWT_ERR;
import static ggs.srr.exception.security.authentication.ErrorCode.JWT_ERR;

@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access_expire_ms}")
    private Long accessExpireMs;

    @Value("${jwt.refresh_expire_ms}")
    private Long refreshExpireMs;

    public JwtTokenResponse createJwtToken(CreateJwtRequest request, Long nowMs) {

        String accessToken = createAccessToken(request, nowMs,true);
        String refreshToken = createAccessToken(request, nowMs, false);

        return new JwtTokenResponse(accessToken, refreshToken, accessExpireMs / 1000);
    }

    private String createAccessToken(CreateJwtRequest request, Long nowMs,boolean isAccessToken) {
        long expireMs = isAccessToken ? accessExpireMs : refreshExpireMs;

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .issuedAt(new Date(nowMs))
                .issuer("SRR")
                .claim("intraId", request.getIntraId())
                .claim("role", request.getRole().getText())
                .expiration(new Date(nowMs + expireMs))
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String accessToken) {
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(accessToken);
        } catch (ExpiredJwtException e) {
            log.error("JwtUtils.validateToken: 만료된 토큰입니다.");
            throw new AuthenticationException(EXPIRED_JWT_ERR);
        } catch (JwtException e) {
            log.error("JwtUtils.validateToken: jwt token 검증시 오류가 발생했습니다.");
            throw new AuthenticationException(JWT_ERR);
        }

    }
}
