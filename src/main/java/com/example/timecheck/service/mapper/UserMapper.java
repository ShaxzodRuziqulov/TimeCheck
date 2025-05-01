package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.User;
import com.example.timecheck.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = JobMapper.class)
public interface UserMapper {

    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "job.id", target = "jobId")
    UserDto toDto(User user);

    @Mapping(source = "roleId", target = "role.id")
    @Mapping(source = "jobId", target = "job.id")
    User toEntity(UserDto userDto);
}
