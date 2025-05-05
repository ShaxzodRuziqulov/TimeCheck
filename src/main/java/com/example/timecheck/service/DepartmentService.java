package com.example.timecheck.service;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.enumirated.DepartmentStatus;
import com.example.timecheck.repository.DepartmentRepository;
import com.example.timecheck.service.dto.DepartmentDto;
import com.example.timecheck.service.mapper.DepartmentMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    public DepartmentService(DepartmentRepository departmentRepository,
                             DepartmentMapper departmentMapper) {
        this.departmentRepository = departmentRepository;
        this.departmentMapper = departmentMapper;
    }

    public DepartmentDto create(DepartmentDto departmentDto) {
        Department department = departmentMapper.toEntity(departmentDto);
        if (department.getDepartmentStatus() == null) {
            department.setDepartmentStatus(DepartmentStatus.ACTIVE);
        }

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }

    public DepartmentDto update(DepartmentDto departmentDto) {
        Department department = departmentRepository.findById(departmentDto.getId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setName(departmentDto.getName());
        department.setDepartmentStatus(departmentDto.getDepartmentStatus());

        department = departmentRepository.save(department);
        return departmentMapper.toDto(department);
    }


    public List<DepartmentDto> findAll() {
        return departmentRepository.findAll()
                .stream()
                .map(departmentMapper::toDto)
                .collect(Collectors.toList());
    }

    public DepartmentDto findById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        return departmentMapper.toDto(department);
    }

    public Department delete(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        department.setDepartmentStatus(DepartmentStatus.DELETED);
        return departmentRepository.save(department);
    }

    public List<DepartmentDto> findByActiveDepartment() {
        return departmentRepository.findByStatus(DepartmentStatus.ACTIVE)
                .stream().map(departmentMapper::toDto).collect(Collectors.toList());
    }

    public long countByActiveDepartment() {
        return departmentRepository.countByDepartmentStatus(DepartmentStatus.ACTIVE);
    }
}
