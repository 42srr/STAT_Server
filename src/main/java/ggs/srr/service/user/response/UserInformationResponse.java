package ggs.srr.service.user.response;

import ggs.srr.domain.user.User;
import lombok.Getter;

@Getter
public class UserInformationResponse {
    private Long id;
    private String intraId;
    private double level;
    private int wallet;
    private int collectionPoint;
    private String imgURL;
    private boolean updatable;

    public UserInformationResponse(User user, boolean updatable) {
        this.id = user.getId();
        this.intraId = user.getIntraId();
        this.level = user.getLevel();
        this.wallet = user.getWallet();
        this.collectionPoint = user.getCorrectionPoint();
        this.imgURL = user.getImage();
        this.updatable = updatable;
    }
}
