package ggs.srr.api.controller.level.dto;
import lombok.Data;

@Data
public class ProjectsDto {

    private String projectName;
    private int count;

    public ProjectsDto(String name, int count) {
        this.projectName = name;
        this.count = count;
    }
}
