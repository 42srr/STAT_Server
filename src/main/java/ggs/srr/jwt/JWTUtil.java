package ggs.srr.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

//    public static final long ACCESS_TOKEN_EXPIRE_MS = 30  * 60 * 1000L;
    public static final long ACCESS_TOKEN_EXPIRE_MS = 30 * 60 * 1000L;
    public static final long REFRESH_TOKEN_EXPIRE_MS = 24 * 60 * 60 * 1000L;

    private final SecretKey secretKey;

    public JWTUtil(@Value("${jwt.secret}")String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    // 검증 메서드

    public String getIntraId(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("intraId", String.class);
    }

    public String getRole(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public boolean isExpired(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    // token 생성 메서드

    public String createJWT(String intraId, String role, long expiredMs){
        return Jwts.builder()
                .claim("intraId", intraId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }
}
