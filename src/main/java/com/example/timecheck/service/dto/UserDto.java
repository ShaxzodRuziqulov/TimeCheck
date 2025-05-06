package com.example.timecheck.service.dto;

import com.example.timecheck.entity.enumirated.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Builder
@Data
public class UserDto {
    private Long id;

    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;

    private String username;

    private String password;

    private Set<RoleDTO> roles;

    private Long jobId;

    private UserStatus userStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

