package ggs.srr.repository.user.dto;

import ggs.srr.service.user.RankingType;
import lombok.Getter;

@Getter
public class UserRankQueryDto {
    private int startPosition;
    private int maxResult;

    public UserRankQueryDto(int startPosition, int maxResult) {
        this.startPosition = startPosition;
        this.maxResult = maxResult;
    }
}
