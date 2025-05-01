package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.Job;
import com.example.timecheck.service.dto.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = DepartmentMapper.class)
public interface JobMapper {
    @Mapping(source = "department.id", target = "departmentId")
    JobDto toDto(Job job);

    @Mapping(source = "departmentId", target = "department.id")
    Job toEntity(JobDto dto);
}
