package com.project.gymweb.controllers;

import com.project.gymweb.dto.create.UserRegisterDTO;
import com.project.gymweb.dto.view.UserRO;
import com.project.gymweb.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("Users")
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<UserRO>> users() {
        var users = userService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRO> getUserById(@PathVariable UUID id) {
        var user = userService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<UserRO> getUserByUsernameRO(@PathVariable String username) {
        var user = userService.findByUsernameRO(username);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserRO> getUserByEmail(@PathVariable String email) {
        var user = userService.findByEmailRO(email);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserRO> updateUser(@PathVariable UUID id, @RequestBody UserRegisterDTO userRegisterDTO) {
        var user = userService.updateUser(id, userRegisterDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
