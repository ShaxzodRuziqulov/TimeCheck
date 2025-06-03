package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Role;
import com.example.timecheck.entity.User;
import com.example.timecheck.responce.LoginResponse;
import com.example.timecheck.service.AuthenticationService;
import com.example.timecheck.service.JwtService;
import com.example.timecheck.service.dto.LoginUserDto;
import com.example.timecheck.service.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AuthenticationResource(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        UserDto result = authenticationService.signUp(userDto);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.login(loginUserDto);
        String token = jwtService.generateToken(authenticatedUser);

        List<String> roles = authenticatedUser.getRoles()
                .stream()
                .map(Role::getName)
                .toList();
        LoginResponse loginResponse = new LoginResponse()
                .setFirstName(authenticatedUser.getFirstName())
                .setLastName(authenticatedUser.getLastName())
                .setMiddleName(authenticatedUser.getMiddleName())
                .setToken(token)
                .setExpiresIn(jwtService.getExpirationTime())
                .setUserId(authenticatedUser.getId())
                .setRole(roles);
        return ResponseEntity.ok(loginResponse);
    }
}
