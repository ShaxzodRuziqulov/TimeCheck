package com.example.timecheck.repository;

import com.example.timecheck.entity.Department;
import com.example.timecheck.entity.enumirated.DepartmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE d.departmentStatus = :departmentStatus")
    List<Department> findByStatus(@Param("departmentStatus") DepartmentStatus departmentStatus);
}
