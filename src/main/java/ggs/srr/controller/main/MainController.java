package ggs.srr.controller.main;

import ggs.srr.controller.main.dto.LevelDto;
import ggs.srr.controller.main.dto.ProjectUserInfoDto;
import ggs.srr.controller.main.dto.ProjectsDto;
import ggs.srr.controller.user.dto.RankingWalletDto;
import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.user.UserService;
import lombok.AllArgsConstructor;

import ggs.srr.controller.user.dto.RankingEvalPointDto;
import ggs.srr.controller.user.dto.RankingWalletDto;
import ggs.srr.service.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final RankingService rankingService;
    private final ProjectRepository projectRepository;

    @GetMapping("/levels")
    public LevelDto levelUserCounts() { return userService.getLevelInfo(); }

    @GetMapping("/ranking/evalpoint")
    public List<RankingEvalPointDto> rankingEvalPointInfo() { return rankingService.rankingEvalPoint(); }


    @GetMapping("/ranking/wallet")
    public List<RankingWalletDto> rankingWalletInfo() { return rankingService.rankingWallet(); }

    @GetMapping("/projects/{intraId}")
    public List<ProjectUserInfoDto> projectUserInfo(@PathVariable String intraId) {
        Optional<FtUser> ftUser = userService.findByIntraId(intraId);
        List<ProjectUser> projectUsers = ftUser.get().getProjectUsers();
        List<ProjectUserInfoDto> res = new ArrayList<>();
        for(ProjectUser projectUser : projectUsers) {
            res.add(new ProjectUserInfoDto(projectUser.getStatus(), projectUser.getProject().getName()));
        }
        return res;
    }

    @GetMapping("/projects")
    public List<ProjectsDto> projectInfo() {
        List<ProjectsDto> res = new ArrayList<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            int count = 0;
            String projectName = project.getName();
            List<ProjectUser> projectUsers = project.getProjectUsers();
            for (ProjectUser projectUser : projectUsers) {
                if (projectUser.getStatus().equals("in_progress")){
                    count++;
                }
            }
            ProjectsDto projectsDto = new ProjectsDto(project.getName(), count);
            res.add(projectsDto);
        }
        return res;
    }
}
