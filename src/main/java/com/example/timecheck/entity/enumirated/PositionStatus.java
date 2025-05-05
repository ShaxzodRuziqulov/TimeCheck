package com.example.timecheck.entity.enumirated;

import lombok.Getter;

@Getter
public enum PositionStatus {
    BACKEND("BACKEND"),
    FRONTEND("FRONTEND"),
    ;

    private final String label;

    PositionStatus(String label) {
        this.label = label;
    }

}
