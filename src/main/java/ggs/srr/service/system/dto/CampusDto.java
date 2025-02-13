package ggs.srr.service.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CampusDto {
    private String name;

    public CampusDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
