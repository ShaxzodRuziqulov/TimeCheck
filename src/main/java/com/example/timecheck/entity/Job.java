package com.example.timecheck.entity;

import com.example.timecheck.entity.enumirated.JobStatus;
import com.example.timecheck.entity.enumirated.PositionStatus;
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
    @Enumerated(EnumType.STRING)
    private PositionStatus positionStatus;
    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;
    @OneToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
