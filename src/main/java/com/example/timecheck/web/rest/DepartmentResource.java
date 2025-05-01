package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Department;
import com.example.timecheck.service.DepartmentService;
import com.example.timecheck.service.dto.DepartmentDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/department")
public class DepartmentResource {
    private final DepartmentService departmentService;

    public DepartmentResource(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/create")
    public ResponseEntity<DepartmentDto> addDepartment(@RequestBody DepartmentDto departmentDto) throws URISyntaxException {
        DepartmentDto result = departmentService.create(departmentDto);
        return ResponseEntity.created(new URI("/api/admin/department/create" + result.getId())).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDto departmentDto) throws URISyntaxException {
        if (!departmentDto.getId().equals(id) && departmentDto.getId() != 0) {
            return ResponseEntity.badRequest().body("Invalid id");
        }
        DepartmentDto result = departmentService.update(departmentDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDepartments() {
        List<DepartmentDto> departments = departmentService.findAll();
        return ResponseEntity.ok().body(departments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepartment(@PathVariable Long id) {
        DepartmentDto department = departmentService.findById(id);
        return ResponseEntity.ok().body(department);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDepartment(@PathVariable Long id) {
        Department result = departmentService.delete(id);
        return ResponseEntity.ok().body(result);
    }
}
