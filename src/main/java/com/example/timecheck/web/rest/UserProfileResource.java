package com.example.timecheck.web.rest;

import com.example.timecheck.entity.User;
import com.example.timecheck.responce.PasswordResponse;
import com.example.timecheck.service.UserService;
import com.example.timecheck.service.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserProfileResource {
    private final UserService userService;

    public UserProfileResource(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/update-profile/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_USER', 'ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody UserDto userDto, @PathVariable Long id) throws URISyntaxException {
        if (!userDto.getId().equals(id) && userDto.getId() != 0) {
            return ResponseEntity.badRequest().body("invalid id");
        }
        UserDto result = userService.update(userDto);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/activeUser")
    public ResponseEntity<?> findAllUsers() {
        List<UserDto> result = userService.allActiveUsers();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-password/{userId}")
    public ResponseEntity<?> updatePassword(@RequestBody PasswordResponse passwordResponse, @PathVariable Long userId) {
        User user = userService.updatePassword(userId, passwordResponse.getOldPassword(), passwordResponse.getNewPassword());
        return ResponseEntity.ok(user);
    }

}
