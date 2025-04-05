package ggs.srr.service.user.request;

import lombok.Getter;

@Getter
public class UserRankingServiceRequest {

    private int startPosition;
    private int maxResult;

    public UserRankingServiceRequest(int startPosition, int maxResult) {
        this.startPosition = startPosition;
        this.maxResult = maxResult;
    }
}
