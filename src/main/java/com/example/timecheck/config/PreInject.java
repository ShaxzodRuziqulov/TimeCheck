package com.example.timecheck.config;

import com.example.timecheck.entity.*;
import com.example.timecheck.entity.enumirated.DepartmentStatus;
import com.example.timecheck.entity.enumirated.JobStatus;
import com.example.timecheck.entity.enumirated.UserStatus;
import com.example.timecheck.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PreInject {

    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PositionRepository positionRepository;
    private final DepartmentRepository departmentRepository;
    private final JobRepository jobRepository;


    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private Role createRole(String name, String description) {
        return Role.builder()
                .name(name)
                .description(description)
                .build();
    }

    @PostConstruct
    @Transactional
    public void setDefaultUsers() {
        if (roleRepository.count() == 0) {
            List<Role> roles = new ArrayList<>();
            roles.add(createRole("ROLE_ADMIN", "Admin"));
            roles.add(createRole("ROLE_USER", "User"));
            roleRepository.saveAll(roles);
        }

        if (userRepository.count() == 0) {
            Position position = new Position();
            position.setName("Position");

            Department department = new Department();
            department.setName("Department");
            department.setDepartmentStatus(DepartmentStatus.ACTIVE);

            position = positionRepository.save(position);
            department = departmentRepository.save(department);

            Job job = new Job();
            job.setPosition(position);
            job.setDepartment(department);
            job.setJobStatus(JobStatus.ACTIVE);
            job = jobRepository.save(job);

            User user = new User();
            user.setUsername("admin");

            Role roleAdmin = roleRepository.findByName("ROLE_ADMIN");
            user.setRoles(Set.of(roleAdmin));
            user.setFirstName("first_name");
            user.setLastName("last_name");
            user.setMiddleName("mid_name");
            user.setBirthDate(LocalDate.now());
            user.setUserStatus(UserStatus.ACTIVE);
            user.setPassword(encodePassword("123"));
            user.setJob(job);
            userRepository.save(user);
        }

    }
}
