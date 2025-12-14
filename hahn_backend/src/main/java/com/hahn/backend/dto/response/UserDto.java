package com.hahn.backend.dto.response;

import lombok.Builder;

@Builder
public class UserDto {
    private Long id;
    private String username ;
    private String email ;
}
