package com.dreamtech.api_gateway.security.dto;

import com.dreamtech.api_gateway.security.model.entities.Role;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    @Column(nullable = false)
    private boolean locked;
    @Column(nullable = false)
    private boolean disabled;
    private Set<Role> roles;
}
