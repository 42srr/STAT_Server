package ggs.srr.service.client.dto;

import java.util.List;
import lombok.Data;

@Data
public class UserUpdateDto {
    private double level;
    private int wallet;
    private int correction_point;
    private List<ProjectDetailInfo> projects;

    public UserUpdateDto(double level, int correction_point, int wallet, List<ProjectDetailInfo> projects) {
        this.level = level;
        this.correction_point = correction_point;
        this.wallet = wallet;
        this.projects = projects;
    }
}
