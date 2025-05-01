package com.example.timecheck.service;

import com.example.timecheck.entity.User;
import com.example.timecheck.entity.enumirated.UserStatus;
import com.example.timecheck.repository.UserRepository;
import com.example.timecheck.service.dto.UserDto;
import com.example.timecheck.service.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto update(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public List<UserDto> allUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors
                        .toList());
    }

    public User findUserId(Long id) {
        return userRepository
                .findById(id)
                .orElseGet(User::new);
    }

    public User deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(UserStatus.DELETED);

        return userRepository.save(user);
    }
}