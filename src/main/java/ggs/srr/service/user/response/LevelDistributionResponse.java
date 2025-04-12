package ggs.srr.service.user.response;

import lombok.Getter;

import java.util.Map;

@Getter
public class LevelDistributionResponse {

    private Map<Integer, Long> distribution;

    public LevelDistributionResponse(Map<Integer, Long> distribution) {
        this.distribution = distribution;
    }
}
