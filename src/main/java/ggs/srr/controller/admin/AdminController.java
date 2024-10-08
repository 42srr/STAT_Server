package ggs.srr.controller.admin;

import ggs.srr.controller.admin.dto.RequestProjectDto;
import ggs.srr.controller.admin.dto.ResponseProjectDto;
import ggs.srr.domain.project.Project;
import ggs.srr.service.project.ProjectService;
import ggs.srr.service.system.InitDataManager;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class AdminController {


    private final InitDataManager initDataManager;
    private final ProjectService projectService;

    @Autowired
    public AdminController(InitDataManager initDataManager, ProjectService projectService) {
        this.initDataManager = initDataManager;
        this.projectService = projectService;
    }

    @GetMapping("/admin/init_data")
    public String initData(HttpServletRequest request) {

        String authorization = request.getHeader("Authorization");
        String token = authorization.split(" ")[1];
        initDataManager.initData(token);

        return "init user data";
    }

    @PostMapping("/admin/projects")
    public ResponseProjectDto registerProject(@RequestBody RequestProjectDto dto) {
        String name = dto.getName();
        projectService.save(new Project(name));
        return new ResponseProjectDto("Ok", "create project: " + name);
    }


}
