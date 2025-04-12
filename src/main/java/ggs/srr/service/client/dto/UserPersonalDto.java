package ggs.srr.service.client.dto;

import lombok.Data;

@Data
public class UserPersonalDto {
    int ft_server_id;
    String intra_id;
    String role;
    int wallet;
    int correction_point;

    public UserPersonalDto(int ft_server_id, String intra_id, String role, int wallet, int correction_point) {
        this.ft_server_id = ft_server_id;
        this.intra_id = intra_id;
        this.role = role;
        this.wallet = wallet;
        this.correction_point = correction_point;
    }
}
