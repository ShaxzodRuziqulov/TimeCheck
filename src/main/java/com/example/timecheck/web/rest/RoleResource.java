package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Role;
import com.example.timecheck.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/role")
public class RoleResource {
    private final RoleService roleService;

    public RoleResource(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles() {
        List<Role> getAll = roleService.findAllRoles();
        return ResponseEntity.ok(getAll);
    }
}
