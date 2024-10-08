package ggs.srr.controller.main;

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
    public Map<Integer, Integer> levelUserCounts() { return userService.getLevelInfo(); }

    @GetMapping("/ranking/evalpoint")
    public List<RankingEvalPointDto> rankingEvalPointInfo() { return rankingService.rankingEvalPoint(); }


    @GetMapping("/ranking/wallet")
    public List<RankingWalletDto> rankingWalletInfo() { return rankingService.rankingWallet(); }

    @GetMapping("/projects/{intraId}")
    public String projectUserInfo(@PathVariable String intraId) {
        Optional<FtUser> ftUser = userService.findByIntraId(intraId);
        List<ProjectUser> projectUsers = ftUser.get().getProjectUsers();
        log.info("project size = {}", projectUsers.size());
        projectUsers.forEach(p -> {
            if (!p.getProject().getName().contains("Exam") && p.getStatus().equals("in_progress"))
                log.info("now = {}", p.getProject().getName());
        });
        return ("ok");
    }

    @GetMapping("/projects")
    public Map<String, Integer> projectInfo() {
        Map<String, Integer> map = new HashMap<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            int count = 0;
            String name = project.getName();
            List<ProjectUser> users = project.getProjectUsers();

            for(ProjectUser user : users) {
                if (user.getUser().getLevel() != 0.0 && user.getStatus().equals("in_progress")) {
                    String intraId = user.getUser().getIntraId();
                    log.info("intra id = {} project = {}", intraId, project.getName());
                    count++;
                }
            }
            map.put(name, count);
        }
        return map;
    }
}
