package ggs.srr.controller.home.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class QuotesDto {

    private Long    id;
    private String name;
    private String content;

    public QuotesDto(Long id, String name, String content) {
        this.id = id;
        this.name = name;
        this.content = content;
    }
}
