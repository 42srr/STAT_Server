package ggs.srr.service.initdata;

import ggs.srr.domain.FtUser;
import ggs.srr.domain.Project;
import ggs.srr.domain.ProjectUser;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.project_user.ProjectUserRepository;
import ggs.srr.repository.user.FtUserRepository;
import ggs.srr.service.initdata.dto.ProjectDto;
import ggs.srr.service.user.FtUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class InitDataService {

    private static int i = 1;
    private final String FT_BASE_URI = "https://api.intra.42.fr";
    private final FtUserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final ProjectUserRepository projectUserRepository;
    public void initUserAndProjectData(OAuth2AuthorizedClient client) throws InterruptedException {
        String accessTokenValue = "Bearer " + client.getAccessToken().getTokenValue();
        List<Long> resourceOwnerIdList = new ArrayList<>();
        URI uri;
        RestTemplate restTemplate;
        int page = 1;
        RequestEntity<Void> requestEntity;
        log.info("start downloading user data ...");

        while(true){
            uri = UriComponentsBuilder
                    .fromUriString(FT_BASE_URI)
                    .path("/v2/campus/69/users")
                    .queryParam("page", page)
                    .queryParam("per_page", 100)
                    .queryParam("campus", 69)
                    .encode()
                    .build()
                    .toUri();

            requestEntity = RequestEntity
                    .get(uri)
                    .header("Authorization", accessTokenValue)
                    .build();
            restTemplate = new RestTemplate();
            ResponseEntity<Object> exchange = restTemplate.exchange(requestEntity, Object.class);
            List<LinkedHashMap<String, Object>> temp  = (List<LinkedHashMap<String, Object>>)exchange.getBody();
            if(temp.isEmpty())
                break;

            temp.forEach((data) ->{
                String fistName = data.get("first_name").toString();
                String lastName = data.get("last_name").toString();

                if(!(fistName.equals("Test") || lastName.equals("Test")) && !(fistName.equals("Temp") || lastName.equals("Temp")))
                    resourceOwnerIdList.add(Long.parseLong(data.get("id").toString()));
            }
            );
            log.info("downloading ...");
            page++;
            Thread.sleep(500);
        }
        log.info("==== load id complete ====");
        log.info("size = {}", resourceOwnerIdList.size());

        resourceOwnerIdList.forEach((id) -> {

            try{
                log.info("[{} / {}] download", i++, resourceOwnerIdList.size());
                fillUserAndProject(id, accessTokenValue);
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        });
    }

    private void fillUserAndProject(Long id, String accessTokenValue) throws InterruptedException {
        log.info("start filling data to db");
        URI uri = UriComponentsBuilder
                .fromUriString(FT_BASE_URI)
                .path("/v2/users/" + id)
                .encode()
                .build()
                .toUri();

        RequestEntity<Void> requestEntity = RequestEntity
                .get(uri)
                .header("Authorization", accessTokenValue)
                .build();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Object> result = restTemplate.exchange(requestEntity, Object.class);

        LinkedHashMap<String, Object> resultData = (LinkedHashMap<String, java.lang.Object>)result.getBody();
        parseData(resultData);
        Thread.sleep(500);
    }

    private void parseData(LinkedHashMap<String, Object> data){
        FtUser ftUser = parseUser(data);
        userRepository.save(ftUser);
        List<ProjectDto> projects = parseProjects(data);
        System.out.println(projects);

        projects.forEach(projectDto -> {
            Project project = projectDto.getProject();
            String status = projectDto.getStatus();
            ProjectUser projectUser = new ProjectUser();
            projectUser.addProjectAndUser(ftUser, project, status);
            projectUserRepository.save(projectUser);
        });

    }

    private List<ProjectDto> parseProjects(LinkedHashMap<String, Object> data){
        List<ProjectDto> projects = new ArrayList<>();
        List<LinkedHashMap<String, Object>> projectsUsers = (List<LinkedHashMap<String, Object>>) data.get("projects_users");
        if (projectsUsers.isEmpty())
                return projects;
        projectsUsers.forEach((projectData) -> {
            LinkedHashMap<String, Object> projectValue = (LinkedHashMap<String, Object>) projectData.get("project");
            long resourceId = Long.parseLong(projectValue.get("id").toString());
            Project project = projectRepository.findByResourceId(resourceId);
            String status = projectData.get("status").toString();
            if (project == null) {
                String name = projectValue.get("name").toString();
                Project newProject = new Project(resourceId, name);
                project = newProject;
                projectRepository.save(newProject);
            }
            ProjectDto projectDto = new ProjectDto(status, project);
            projects.add(projectDto);
        } );
        return projects;
    }

    private FtUser parseUser(LinkedHashMap<String, Object> data){

        double level = 0L;
        Object cursusUsers = data.get("cursus_users");
        List<LinkedHashMap<String, Object>> usersData = (List<LinkedHashMap<String, Object>>)cursusUsers;
        System.out.println(usersData);
        for (LinkedHashMap<String, Object> user: usersData) {
            if (user.get("grade") != null && user.get("grade").toString().equals("Learner")){
                level = Double.parseDouble(user.get("level").toString());
            }
        }
        Long resourceOwnerId = Long.parseLong(data.get("id").toString());
        String email = data.get("email").toString();
        String intraId = data.get("login").toString();
        LinkedHashMap<String, Object> imageData = (LinkedHashMap<String, Object>)data.get(("image"));
        String imgUri = getImgUri(imageData);
        int correction_point = Integer.parseInt(data.get("correction_point").toString());
        int wallet = Integer.parseInt(data.get("wallet").toString());
        return new FtUser(level, resourceOwnerId, email, intraId, imgUri,correction_point, wallet);
    }

    private String getImgUri(LinkedHashMap<String, Object> imageData){
        String imgUri = null;
        try{
            imgUri =  imageData.get("link").toString();
        } catch (NullPointerException e){
        }
        return imgUri;
    }





}
