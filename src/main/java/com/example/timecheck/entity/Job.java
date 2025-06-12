package com.example.timecheck.entity;

import com.example.timecheck.entity.enumirated.JobStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Job extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
