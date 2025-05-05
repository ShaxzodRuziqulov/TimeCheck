package com.example.timecheck.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;

    private LocalDateTime sentAt;

    @ManyToOne
    private User recipient;

    private boolean sentSuccessfully;
}
