package ggs.srr.service.projectuser;

import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.projectuser.ProjectUserStatus;
import ggs.srr.repository.projectuser.ProjectUserRepository;
import ggs.srr.service.projectuser.response.ProjectUserDistributionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectUserService {

    private final ProjectUserRepository projectUserRepository;

    public ProjectUserDistributionResponse getProjectUserDistribution() {

        Map<String, Map<ProjectUserStatus, Long>> distribution = projectUserRepository.findAll().stream()
                .collect(groupingBy(projectUser -> projectUser.getProject().getName(), groupingBy(
                        ProjectUser::getStatus, counting()
                )));

        initializeExcludedStatus(distribution);

        return new ProjectUserDistributionResponse(distribution);
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
