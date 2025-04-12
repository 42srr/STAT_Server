package ggs.srr.service.user.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserFtIdAndIntraIdResponse {

    private Long ftServerId;
    private String intraId;

    public UserFtIdAndIntraIdResponse(Long ftServerId, String intraId) {
        this.ftServerId = ftServerId;
        this.intraId = intraId;
    }
}
