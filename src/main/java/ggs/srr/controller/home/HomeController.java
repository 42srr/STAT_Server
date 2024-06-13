package ggs.srr.controller.home;

import ggs.srr.controller.home.dto.QuotesDto;
import ggs.srr.domain.FtUser;
import ggs.srr.domain.Project;
import ggs.srr.domain.ProjectUser;
import ggs.srr.domain.Quotes;
import ggs.srr.repository.project.ProjectRepository;
import ggs.srr.repository.user.FtUserRepository;
import ggs.srr.service.quotes.QuotesService;
import ggs.srr.service.user.FtUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping(value = "/", produces = "application/json;charset=UTF-8")
@RequiredArgsConstructor
public class HomeController {

    private final QuotesService quotesService;
    private final FtUserService userService;
    private final ProjectRepository projectRepository;
    private final FtUserRepository ftUserRepository;


    @GetMapping("/")
    public String home(@RegisteredOAuth2AuthorizedClient("42")OAuth2AuthorizedClient client){

        OAuth2AccessToken accessToken = client.getAccessToken();
        OAuth2RefreshToken refreshToken = client.getRefreshToken();
        log.info("accessToken = {}", accessToken.getTokenValue());
        log.info("refreshToken = {}",refreshToken.getTokenValue());
        return client.getPrincipalName();
    }

    @GetMapping("/projects/{intraId}")
    public String projects(@PathVariable(name = "intraId") String intraId){

        FtUser user = userService.findByIntraId(intraId);
        List<ProjectUser> projects = user.getProjects();
        log.info("project size = {}", projects.size());
        projects.forEach(p -> {
            if(p.getStatus().equals("in_progress"))
                log.info("now = {}", p.getProject().getName());
        });
        return "ok";
    }

    @GetMapping("/projects")
    public Map<String, Integer> projectInfo(){
        Map<String, Integer> map = new HashMap<>();
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            int count=  0;
            String name = project.getName();
            log.info("project = {}", name);
            List<ProjectUser> users = project.getUsers();
            for(ProjectUser user : users){
                if(user.getStatus().equals("in_progress"))
                {
                    String intraId = user.getFtUser().getIntraId();
                    log.info("intra id = {}", intraId);
                    count++;
                }
            }
            map.put(name, count);
        }
        System.out.println(map);
        return map;
    }

    @GetMapping("/levels")
    public Map<Integer, Integer> levelInfo() {
        return userService.levelDistributionInfo();
    }


    @GetMapping("/quotes")
    public QuotesDto getQuotes(){
        Quotes quotes = quotesService.getQuotes();
        return new QuotesDto(quotes.getId(), quotes.getName(),  quotes.getContent());
    }

    @GetMapping("/quotes/{id}")
    public QuotesDto getQuotesById(@PathVariable Long id) {
        Quotes quotes = quotesService.getQuotesById(id);
        return new QuotesDto(quotes.getId(), quotes.getName(), quotes.getContent());
    }

    @PostMapping("/quotes")
    public Long addQuotes(@RequestBody QuotesDto quotesDto){
        Quotes quotes = new Quotes(null, quotesDto.getName(), quotesDto.getContent());
        quotesService.addQuotes(quotes);
        return quotes.getId();
    }

    @GetMapping("/quotes/count")
    public String getQuoteSize(){
        int quotesNumber = quotesService.getQuotesSize();
        return String.valueOf(quotesNumber);
    }

    @DeleteMapping("/quotes/{quotesId}")
    public ResponseEntity<Quotes> deleteQuotes(@PathVariable("quotesId") Long quotesId){
        Quotes deleted = quotesService.deleteQuotes(quotesId);
        return (deleted != null) ?
                ResponseEntity.status(HttpStatus.NO_CONTENT).build() :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


}
