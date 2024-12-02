package ggs.srr.api.controller.main;

import ggs.srr.api.ApiResponse;
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

    /**
     * DTO 등 수정 필요
     * 컨트롤러 분리 필요
     */

    private final UserService userService;
    private final RankingService rankingService;

    @GetMapping("/levels")
    public ApiResponse<LevelDto> levelUserCounts() {
        return ApiResponse.ok(userService.getLevelInfo());
    }

    @GetMapping("/ranking/evalpoint")
    public ApiResponse<List<RankingEvalPointDto>> rankingEvalPointInfo() {
        return ApiResponse.ok(rankingService.rankingEvalPoint());
    }


    @GetMapping("/ranking/wallet")
    public ApiResponse<List<RankingWalletDto>> rankingWalletInfo() {
        return ApiResponse.ok(rankingService.rankingWallet());
    }

}
