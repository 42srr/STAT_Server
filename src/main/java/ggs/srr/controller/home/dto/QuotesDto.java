package ggs.srr.controller.home.dto;

import lombok.Data;
import lombok.Getter;

@Data
public class QuotesDto {

    private String name;
    private String content;

    public QuotesDto(String name, String content) {
        this.name = name;
        this.content = content;
    }
}
