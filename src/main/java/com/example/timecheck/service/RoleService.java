package com.example.timecheck.service;

import com.example.timecheck.entity.Role;
import com.example.timecheck.repository.RoleRepository;
import com.example.timecheck.service.mapper.RoleMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }
}
