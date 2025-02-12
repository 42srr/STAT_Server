package ggs.srr.service.system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    int ft_server_id;
    String intra_id;
    String role;
    int wallet;
    int correction_point;
    double level;
    String image;
}
