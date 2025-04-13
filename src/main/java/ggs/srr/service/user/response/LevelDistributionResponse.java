package ggs.srr.service.user.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.Map;

@Getter
@Schema(description = "사용자 레벨 분포")
public class LevelDistributionResponse {

    @Schema(description = "key : 레벨, value : 해당 레벨 인원 수")
    private Map<Integer, Long> distribution;

    public LevelDistributionResponse(Map<Integer, Long> distribution) {
        this.distribution = distribution;
    }
}
