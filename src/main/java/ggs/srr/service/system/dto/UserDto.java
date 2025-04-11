package ggs.srr.service.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    long ft_server_id;
    String intra_id;
    String role;
    int wallet;
    int correction_point;
    double level;
    String image;
}
