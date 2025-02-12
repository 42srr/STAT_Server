package ggs.srr.service.system.dto;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {
    private String name;
    private List<CampusDto> campus;

    public ProjectDto(String name, List<CampusDto> campus) {
        this.name = name;
        this.campus = campus;
    }

    public String getName() {
        return name;
    }

    public List<CampusDto> getCampus() {
        return campus;
    }
}
