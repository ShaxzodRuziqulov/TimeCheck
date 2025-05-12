package com.example.timecheck.service.dto;

import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class RoleDTO {

    private Long id;

    private String name;

    private String description;

}
