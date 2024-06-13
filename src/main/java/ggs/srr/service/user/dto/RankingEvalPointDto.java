package ggs.srr.service.user.dto;

import ggs.srr.domain.Project;
import lombok.Data;

@Data
public class RankingEvalPointDto {
    private String photo;
    private String intraId;
    private int evalPoint;

    public RankingEvalPointDto(String photo, String intraId, int evalPoint) {
        this.photo = photo;
        this.intraId = intraId;
        this.evalPoint = evalPoint;
    }
}