package com.example.timecheck.responce;

import lombok.Getter;

import java.util.List;

@Getter
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String middleName;
    private String token;
    private long expiresIn;
    private Long userId;
    private List<String> roles;

    public LoginResponse setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LoginResponse setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

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

    public LoginResponse setRole(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public LoginResponse setMiddleName(String middleName) {
        this.middleName = middleName;
        return this;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", token='" + token + '\'' +
                ", expiresIn=" + expiresIn +
                ", userId=" + userId +
                ", roles='" + roles + '\'' +
                '}';
    }
}
