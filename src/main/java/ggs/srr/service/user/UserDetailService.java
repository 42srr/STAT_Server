package ggs.srr.service.user;

import ggs.srr.security.authentication.response.AuthorizationServerResponse;
import ggs.srr.security.authentication.response.UserDetails;
import ggs.srr.domain.user.User;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.security.jwt.response.JwtTokenResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserDetailService {

    private final UserRepository userRepository;

    public void updateUserDetails(UserDetails details, AuthorizationServerResponse authorizationServerResponse, JwtTokenResponse jwtToken) {
        String intraId = details.getIntraId();
        User findUser = userRepository.findByIntraId(intraId).orElse(createNewUser(details));

        findUser.updateAuthenticationServerToken(authorizationServerResponse.getAccessToken(), authorizationServerResponse.getRefreshToken());
        findUser.updateRefreshToken(jwtToken.getRefreshToken());

        if (!isAlreadyExistsUser(findUser)) {
            findUser.initializeCreateDateTime(LocalDateTime.now());
            userRepository.save(findUser);
        }

    }

    private boolean isAlreadyExistsUser(User user) {
        return user.getId() != null;
    }

    private User createNewUser(UserDetails details) {
        return User.builder()
                .intraId(details.getIntraId())
                .ftServerId(details.getFtServerId())
                .wallet(details.getWallet())
                .collectionPoint(details.getCorrectionPoint())
                .build();
    }
}
