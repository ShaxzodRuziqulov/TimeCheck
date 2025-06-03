package com.example.timecheck.service;

import com.example.timecheck.entity.User;
import com.example.timecheck.entity.enumirated.UserStatus;
import com.example.timecheck.repository.JobRepository;
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
    private final JobRepository jobRepository;

    public UserService(UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder passwordEncoder, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jobRepository = jobRepository;
    }

    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UserDto update(UserDto userDto) {
        User existingUser = userRepository.findById(userDto.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userDto.getId()));

        if (userDto.getPassword() != null && userDto.getPassword().trim().isEmpty()) {
            userDto.setPassword(null);
        }
        userMapper.partialUpdate(existingUser, userDto);

        if (userDto.getPassword() != null && !userDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        existingUser.setJob(jobRepository.findById(userDto.getJobId()).orElseThrow(() -> new RuntimeException("Job not found with id: " + userDto.getJobId())));

        User updatedUser = userRepository.save(existingUser);

        return userMapper.toDto(updatedUser);
    }


    public List<UserDto> allUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors
                        .toList());
    }

    public List<UserDto> allActiveUsers() {
        return userRepository.findAllByUserStatus(UserStatus.ACTIVE)
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

        if (user.getJob() != null) {
            user.setJob(null);
        }
        if (user.getUsername() != null) {
            user.setUsername("deleted_" + user.getId());
        }

        user.setUserStatus(UserStatus.DELETED);

        return userRepository.save(user);
    }

    public void deleted(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    public long countByActiveUser() {
        return userRepository.countByUserStatus(UserStatus.ACTIVE);
    }

}