package com.ggs.STAT_TurboFetch.client.dto;

import lombok.Data;

@Data
public class UserDto {
    int ft_server_id;
    String intra_id;
    String role;
    int wallet;
    int correction_point;
    double level;
    String image;
}
