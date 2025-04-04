package ggs.srr.service.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.service.user.NotFoundUserException;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.response.UserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public UserInformationResponse findById(UserInformationServiceRequest request, LocalDateTime now) {
        User findUser = userRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundUserException("해당 사용자를 찾을 수 없습니다."));

        boolean updatable = isUpdatableUser(findUser, now);
        return new UserInformationResponse(findUser, updatable);
    }

    private boolean isUpdatableUser(User user, LocalDateTime now) {
        return Duration.between(user.getUpdatedAt(), now).toHours() >= 12;
    }

}
