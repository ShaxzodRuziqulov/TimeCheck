package com.example.timecheck.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

@Data
@Entity
public class CheckInWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    private Job job;


}
