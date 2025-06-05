package com.example.timecheck.web.rest;

import com.example.timecheck.entity.Role;
import com.example.timecheck.entity.User;
import com.example.timecheck.responce.LoginResponse;
import com.example.timecheck.service.AuthenticationService;
import com.example.timecheck.service.JwtService;
import com.example.timecheck.service.dto.LoginUserDto;
import com.example.timecheck.service.dto.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return ResponseEntity.ok("Successfully logged out");
    }
}
