package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.Role;
import com.example.timecheck.entity.User;
import com.example.timecheck.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = JobMapper.class)
public interface UserMapper extends EntityMapper<UserDto, User> {

    @Mapping(source = "roles", target = "rolesId")
    @Mapping(source = "job.id", target = "jobId")
    UserDto toDto(User user);

    @Mapping(source = "rolesId", target = "roles")
    @Mapping(source = "jobId", target = "job.id")
    User toEntity(UserDto userDto);

    default Set<Long> mapRolesToIds(Set<Role> roles) {
        return roles.stream()
                .map(Role::getId)
                .collect(Collectors.toSet());
    }

    default Set<Role> mapIdsToRoles(Set<Long> ids) {
        if (ids == null) return new HashSet<>();
        return ids.stream()
                .map(id -> {
                    Role role = new Role();
                    role.setId(id);
                    return role;
                })
                .collect(Collectors.toSet());
    }

}
