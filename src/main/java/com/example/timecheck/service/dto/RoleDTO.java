package com.example.timecheck.service.dto;

import com.example.timecheck.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Builder
@Data
public class RoleDTO {

    private Long id;

    private String name;

    private String description;

    private Set<User> users;
}
