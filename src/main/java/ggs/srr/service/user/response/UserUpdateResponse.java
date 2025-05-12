package ggs.srr.service.user.response;

import ggs.srr.domain.user.User;
import ggs.srr.service.client.dto.ProjectDetailInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Getter
@Schema(description = "업데이트된 사용자 정보 응답")
public class UserUpdateResponse {
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

    @Schema(description = "업데이트 가능여부. 최근 업데이트로 부터 12 시간이후 true 로 변경됨")
    private boolean updatable;

    @Schema(description = "진행중인 과제")
    private List<ProjectDetailInfo> inProgressProjects;

    @Schema(description = "완료한 과제")
    private List<ProjectDetailInfo> finishedProjects;

    public UserUpdateResponse(User user, boolean updatable, List<ProjectDetailInfo> inProgressProjects,
                              List<ProjectDetailInfo> finishedProjects) {

        this.id = user.getId();
        this.intraId = user.getIntraId();
        this.level = user.getLevel();
        this.wallet = user.getWallet();
        this.collectionPoint = user.getCorrectionPoint();
        this.updatable = updatable;
        this.inProgressProjects = inProgressProjects;
        this.finishedProjects = finishedProjects;
    }
}
