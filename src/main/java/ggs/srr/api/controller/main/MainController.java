package ggs.srr.api.controller.main;

import ggs.srr.api.controller.main.dto.LevelDto;
import ggs.srr.api.controller.main.dto.ProjectUserInfoDto;
import ggs.srr.api.controller.main.dto.ProjectsDto;
import ggs.srr.api.controller.user.dto.RankingWalletDto;
import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.domain.user.FtUser;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.user.UserService;

import ggs.srr.api.controller.user.dto.RankingEvalPointDto;
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
            res.add(new ProjectUserInfoDto(projectUser.getStatus().getText(), projectUser.getProject().getName()));
        }
        return res;
    }


}
