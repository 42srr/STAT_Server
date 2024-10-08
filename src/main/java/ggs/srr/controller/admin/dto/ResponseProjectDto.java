package ggs.srr.controller.admin.dto;

import lombok.Data;

@Data
public class ResponseProjectDto {

    private String status;
    private String message;

    public ResponseProjectDto(String status, String message) {
        this.status = status;
        this.message = message;
    }
}
