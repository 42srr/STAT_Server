package ggs.srr.controller.main;

import ggs.srr.domain.project.Project;
import ggs.srr.domain.projectuser.ProjectUser;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.service.user.UserService;
import lombok.AllArgsConstructor;

import ggs.srr.controller.user.dto.RankingEvalPointDto;
import ggs.srr.service.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

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
