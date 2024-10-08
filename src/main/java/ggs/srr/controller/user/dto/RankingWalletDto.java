package ggs.srr.controller.user.dto;

import lombok.Data;

@Data
public class RankingWalletDto {
    private String photo;
    private String intraId;
    private int dollar;

    public RankingWalletDto(String photo, String intraId, int dollar) {
        this.photo = photo;
        this.intraId = intraId;
        this.dollar = dollar;
    }
}
