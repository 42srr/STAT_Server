package ggs.srr.service.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.exception.projectuser.NotFoundProjectUserException;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.service.projectuser.request.ProjectUserRequest;
import ggs.srr.service.projectuser.request.ProjectUsersRequest;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import ggs.srr.service.projectuser.response.ProjectUserInformationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserDistributionResponse getProjectUserDistribution() {

        Map<String, Map<ProjectUserStatus, Long>> distribution = projectUserRepository.findAll().stream()
                .collect(groupingBy(projectUser -> projectUser.getProject().getName(),
                        groupingBy(ProjectUser::getStatus, counting()
                        )));

        initializeExcludedStatus(distribution);

        return new ProjectUserDistributionResponse(distribution);
    }

    public List<ProjectUserInformationResponse> getUserProjects(ProjectUsersRequest request) {

        Long userId = request.getUserId();
        ProjectUserStatus status = request.getStatus();

        List<ProjectUser> projectUsers = null;

        if (status == ProjectUserStatus.DEFAULT) {
            projectUsers = projectUserRepository.findByUserId(userId);
        } else {
            projectUsers = projectUserRepository.findByUserIdAndStatus(userId, status);
        }

        return projectUsers.stream()
                .map(ProjectUserInformationResponse::new)
                .collect(Collectors.toList());

    }

    public ProjectUserInformationResponse getUserProject(ProjectUserRequest request) {

        ProjectUser findProjectUser = projectUserRepository.findByUserIdAdnProjectId(request.getUserId(), request.getProjectId())
                .orElseThrow(() -> new NotFoundProjectUserException("해당 사용자의 프로젝트를 조회할 수 없습니다."));

        return new ProjectUserInformationResponse(findProjectUser);
    }

    private void initializeExcludedStatus(Map<String, Map<ProjectUserStatus, Long>> distribution) {
        distribution.keySet()
                .forEach(projectName -> {
                    Map<ProjectUserStatus, Long> eachStatusCountMap = distribution.get(projectName);
                    Arrays.stream(ProjectUserStatus.values())
                            .forEach(status -> {
                                if (!eachStatusCountMap.containsKey(status)) {
                                    eachStatusCountMap.put(status, 0L);
                                }
                            });
                });
    }
}
