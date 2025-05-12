package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.Role;
import com.example.timecheck.service.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface RoleMapper {

    RoleDTO toDto(Role role);

    Role toEntity(RoleDTO roleDTO);
}
