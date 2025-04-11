package ggs.srr.service.user;

import ggs.srr.domain.user.User;
import ggs.srr.exception.user.UserErrorCode;
import ggs.srr.exception.user.UserException;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.repository.user.dto.UserRankQueryDto;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserFtIdAndIntraIdResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

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
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        boolean updatable = isUpdatableUser(findUser, now);
        return new UserInformationResponse(findUser, updatable);
    }

    public LevelDistributionResponse getUserLevelDistribution() {
        List<User> users = userRepository.findAll();

        Map<Integer, Long> levelDistributionMap = users.stream()
                .collect(
                        groupingBy(user -> (int) Math.floor(user.getLevel()), counting())
                );

        return new LevelDistributionResponse(levelDistributionMap);
    }

    public List<UserInformationResponse> getUserRankingOfLevel(UserRankingServiceRequest request) {

        UserRankQueryDto queryDto = new UserRankQueryDto(request.getStartPosition(), request.getMaxResult());

        return userRepository.getRankByLevel(queryDto).stream()
                .map(u -> new UserInformationResponse(u, isUpdatableUser(u, LocalDateTime.now())))
                .collect(toList());
    }

    public List<UserInformationResponse> getUserRankingOfWallet(UserRankingServiceRequest request) {

        UserRankQueryDto queryDto = new UserRankQueryDto(request.getStartPosition(), request.getMaxResult());

        return userRepository.getRankByWallet(queryDto).stream()
                .map(u -> new UserInformationResponse(u, isUpdatableUser(u, LocalDateTime.now())))
                .collect(toList());
    }

    public List<UserInformationResponse> getUserRankingOfCollectionPoint(UserRankingServiceRequest request) {

        UserRankQueryDto queryDto = new UserRankQueryDto(request.getStartPosition(), request.getMaxResult());

        return userRepository.getRankByCollectionPoint(queryDto).stream()
                .map(u -> new UserInformationResponse(u, isUpdatableUser(u, LocalDateTime.now())))
                .collect(toList());
    }

    private boolean isUpdatableUser(User user, LocalDateTime now) {
        return Duration.between(user.getUpdatedAt(), now).toHours() >= 12;
    }

    public List<UserFtIdAndIntraIdResponse> getAllUsersFtIdAndIntraId() {
        return userRepository.findAll().stream()
                .map(u -> new UserFtIdAndIntraIdResponse(u.getFtServerId(), u.getIntraId()))
                .toList();
    }
}
