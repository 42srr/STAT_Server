package ggs.srr.controller.main;

import ggs.srr.service.user.UserService;
import lombok.AllArgsConstructor;

import ggs.srr.controller.user.dto.RankingEvalPointDto;
import ggs.srr.service.ranking.RankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/levels")
    public Map<Integer, Integer> levelUserCounts() { return userService.getLevelInfo(); }
    private final RankingService rankingService;

    @GetMapping("/ranking/evalpoint")
    public List<RankingEvalPointDto> rankingEvalPointInfo() { return rankingService.rankingEvalPoint(); }

}
