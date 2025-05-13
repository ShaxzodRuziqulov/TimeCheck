package com.example.timecheck.responce;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private long expiresIn;
    private Long userId;
    private String role;


    public LoginResponse setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponse setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public LoginResponse setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public LoginResponse setRole(String role) {
        this.role = role;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "token='" + token + '\'' +
                ", expiresIn=" + expiresIn +
                ", userId=" + userId +
                '}';
    }
}
