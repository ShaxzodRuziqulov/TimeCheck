package com.example.timecheck.entity.enumirated;

import lombok.Getter;

@Getter
public enum PositionStatus {
    BACKEND("BACKEND"),
    FRONTEND("FRONTEND"),
    TEAM_LEAD("TEAM_LEAD"),
    PROJECT_MANAGER("PROJECT_MANAGER"),
    ;

    private final String label;

    PositionStatus(String label) {
        this.label = label;
    }

}
