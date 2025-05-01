package com.example.timecheck.service.mapper;

import com.example.timecheck.entity.Department;
import com.example.timecheck.service.dto.DepartmentDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentDto toDto(Department department);

    Department toEntity(DepartmentDto dto);
}
