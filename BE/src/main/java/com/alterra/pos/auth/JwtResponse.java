package com.alterra.pos.auth;

import com.alterra.pos.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private Integer id;
    private String username;
    private String name;
    private Role role;
}
