package ggs.srr.service.client.dto;

import lombok.Data;
import lombok.Getter;

@Getter
public class ProjectDetailInfo {
    private final String projectName;
    private final Integer projectFinalMark;
    private final String projectStatus;

    public ProjectDetailInfo(String projectName, Integer projectFinalMark, String projectStatus) {
        this.projectName = projectName;
        this.projectFinalMark = projectFinalMark;
        this.projectStatus = projectStatus;
    }
}
