package com.example.timecheck.web.rest;

import com.example.timecheck.entity.User;
import com.example.timecheck.service.UserService;
import com.example.timecheck.service.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
public class UserResource {

    private final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody UserDto userDto) throws URISyntaxException {
        UserDto result = userService.createUser(userDto);
        return ResponseEntity.created(new URI("/api/admin/user/create" + result.getId())).body(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable Long id) throws URISyntaxException {
        if (!userDto.getId().equals(id) && userDto.getId() != 0) {
            return ResponseEntity.badRequest().body("invalid id");
        }
        UserDto result = userService.update(userDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        List<UserDto> result = userService.allUsers();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/activeUser")
    public ResponseEntity<?> findAllUsers() {
        List<UserDto> result = userService.allActiveUsers();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        User user = userService.findUserId(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        User result = userService.deleteUser(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/count")
    public ResponseEntity<?> count() {
        long count = userService.countByActiveUser();
        return ResponseEntity.ok(count);
    }
}