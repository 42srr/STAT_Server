package ggs.srr.service.client.dto;

import lombok.Data;

@Data
public class ParsingResponseDto {
    private String projectName;
    private Integer projectFinalMark;
    private String projectStatus;
}
