package ggs.srr.service.user.response;

import ggs.srr.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "사용자 개인 정보 응답")
public class UserInformationResponse {

    @Schema(description = "사용자 고유 id")
    private Long id;

    @Schema(description = "사용자 intra id")
    private String intraId;

    @Schema(description = "사용자 level")
    private double level;

    @Schema(description = "사용자 알타리안 달러")
    private int wallet;

    @Schema(description = "사용자 보유 평가 포인트")
    private int collectionPoint;

    @Schema(description = "사용자 이미지 url")
    private String imgURL;

    @Schema(description = "업데이트 가능여부. 최근 업데이트로 부터 12 시간이후 true 로 변경됨")
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
