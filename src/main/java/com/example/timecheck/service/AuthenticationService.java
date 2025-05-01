package com.example.timecheck.service;

import com.example.timecheck.entity.User;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.service.dto.LoginUserDto;
import com.example.timecheck.service.dto.UserDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserRepository userRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, UserService userService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public UserDto signUp(UserDto userDto) {
        return userService.createUser(userDto);
    }

    public User login(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getUsername(),
                        loginUserDto.getPassword()
                )
        );
        return userRepository.findByUsername(loginUserDto.getUsername()).orElseThrow();
    }

}
