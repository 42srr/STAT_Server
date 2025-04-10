package ggs.srr.service.user.request;

import lombok.Getter;

@Getter
public class UserRankingServiceRequest {

    private int startPosition;
    private int maxResult;

    public UserRankingServiceRequest(int startPosition, int maxResult) {
        if (startPosition != 0) {
            this.startPosition = startPosition * maxResult + 1;
        } else {
            this.startPosition = 0;
        }
        this.maxResult = maxResult;
    }
}
