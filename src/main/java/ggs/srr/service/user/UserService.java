package ggs.srr.service.user;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.domain.user.User;
import ggs.srr.exception.project.ProjectException;
import ggs.srr.exception.user.UserErrorCode;
import ggs.srr.exception.user.UserException;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.repository.user.UserRepository;
import ggs.srr.repository.user.dto.UserRankQueryDto;
import ggs.srr.service.client.dto.ProjectDetailInfo;
import ggs.srr.service.client.dto.UserProfileUpdate;
import ggs.srr.service.user.request.UserInformationServiceRequest;
import ggs.srr.service.user.request.UserRankingServiceRequest;
import ggs.srr.service.user.request.UserUpdateServiceRequest;
import ggs.srr.service.user.response.LevelDistributionResponse;
import ggs.srr.service.user.response.UserFtIdAndIntraIdResponse;
import ggs.srr.service.user.response.UserInformationResponse;
import ggs.srr.service.user.response.UserUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static ggs.srr.domain.projectuser.ProjectUserStatus.*;
import static ggs.srr.exception.project.ProjectErrorCode.NOT_FOUND_PROJECT;
import static java.util.stream.Collectors.*;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;

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

    public List<UserFtIdAndIntraIdResponse> getAllUsersFtIdAndIntraId() {
        return userRepository.findAll().stream()
                .map(u -> new UserFtIdAndIntraIdResponse(u.getFtServerId(), u.getIntraId()))
                .toList();
    }

    public Long getUserIdByIntraId(String intraId) {
        User findUser = userRepository.findByIntraId(intraId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));
        return findUser.getId();
    }

    @Transactional
    public UserUpdateServiceRequest getUserUpdateRequest(String requesterIntraId, Long targetUserId) {
        User requesterUser = userRepository.findByIntraId(requesterIntraId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        if (requesterUser.getOAuth2AccessToken() == null || requesterUser.getOAuth2RefreshToken() == null) {
            throw new UserException(UserErrorCode.OAUTH_TOKEN_NOT_FOUND);
        }

        return new UserUpdateServiceRequest(
                requesterUser.getOAuth2AccessToken(),
                requesterUser.getOAuth2RefreshToken(),
                targetUser.getFtServerId()
        );
    }

    @Transactional
    public UserUpdateResponse updateUserProfile(Long userId, UserProfileUpdate userProfileUpdate) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.NOT_FOUND_USER));

        user.updateInformation(
                userProfileUpdate.getLevel(),
                userProfileUpdate.getWallet(),
                userProfileUpdate.getCollection_point()
        );

        log.info("delete user's project. userId = {}, intraId = {}",userId, user.getIntraId());
        projectUserRepository.deleteById(userId);

        updateUserProject(user, userProfileUpdate);

        return new UserUpdateResponse(user, false, userProfileUpdate.getIn_progress_projects(), userProfileUpdate.getFinished_projects());
    }

    private boolean isUpdatableUser(User user, LocalDateTime now) {
        return Duration.between(user.getUpdatedAt(), now).toHours() >= 12;
    }

    private void updateUserProject(User user, UserProfileUpdate userProfileUpdate) {
        List<ProjectDetailInfo> inProgressProjects = userProfileUpdate.getIn_progress_projects();
        updateUserProjectRepository(user, inProgressProjects, IN_PROGRESS);

        List<ProjectDetailInfo> finishedProjects = userProfileUpdate.getFinished_projects();
        updateUserProjectRepository(user, finishedProjects, FINISHED);
    }

    private void updateUserProjectRepository(User user, List<ProjectDetailInfo> finishedProjects,
                                             ProjectUserStatus projectUserStatus) {
        for (ProjectDetailInfo finishedProject : finishedProjects) {
            String name = finishedProject.getProjectName();
            Integer finalMark = finishedProject.getProjectFinalMark();

            Project project = projectRepository.findByName(name)
                    .orElseThrow(() -> new ProjectException(NOT_FOUND_PROJECT));

            ProjectUser projectUser = ProjectUser.builder()
                    .user(user)
                    .project(project)
                    .finalMark(finalMark)
                    .status(projectUserStatus)
                    .build();

            projectUserRepository.save(projectUser);
        }
    }

}
