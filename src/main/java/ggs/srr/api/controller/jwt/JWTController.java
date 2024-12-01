package ggs.srr.api.controller.jwt;

import ggs.srr.api.controller.jwt.dto.RefreshTokenDto;
import ggs.srr.domain.user.FtUser;
import ggs.srr.jwt.JWTUtil;
import ggs.srr.oauth.provider.dto.JwtToken;
import ggs.srr.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class JWTController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    public JWTController(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/refresh")
    public JwtToken refresh(HttpServletRequest request, @RequestBody RefreshTokenDto dto) {

        String refreshToken = dto.getRefreshToken();
        String intraId = jwtUtil.getIntraId(refreshToken);

        Optional<FtUser> byIntraId = userService.findByIntraId(intraId);
        if (byIntraId.isEmpty()) {
            throw new RuntimeException("없는 사용자.");
        }
        FtUser user = byIntraId.get();
        if (!refreshToken.equals(dto.getRefreshToken())) {
            throw new RuntimeException("초기화 토큰 불일치");
        }
        String role = getUserRole(intraId);
        String accessToken = jwtUtil.createJWT(intraId, role, JWTUtil.ACCESS_TOKEN_EXPIRE_MS);
        String newRefreshToken = jwtUtil.createJWT(intraId, role, JWTUtil.REFRESH_TOKEN_EXPIRE_MS);
        userService.updateJWTTokens(intraId, accessToken, newRefreshToken);
        
        return new JwtToken(accessToken, newRefreshToken);

    }

    private String getUserRole (String intraId) {
        if (intraId.equals("joojeon") || intraId.equals("jajo")) {
            return "ADMIN";
        }
        return "USER";
    }
}
