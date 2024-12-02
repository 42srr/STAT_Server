package ggs.srr.api.controller.ranking;

import ggs.srr.api.ApiResponse;
import ggs.srr.api.controller.user.dto.RankingEvalPointDto;
import ggs.srr.api.controller.user.dto.RankingWalletDto;
import ggs.srr.service.ranking.RankingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping("/ranking/evalpoint")
    public ApiResponse<List<RankingEvalPointDto>> rankingEvalPointInfo() {
        return ApiResponse.ok(rankingService.rankingEvalPoint());
    }

    @GetMapping("/ranking/wallet")
    public ApiResponse<List<RankingWalletDto>> rankingWalletInfo() {
        return ApiResponse.ok(rankingService.rankingWallet());
    }

}
