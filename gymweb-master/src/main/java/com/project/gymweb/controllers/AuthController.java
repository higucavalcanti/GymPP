package com.project.gymweb.controllers;

import com.project.gymweb.dto.create.UserLoginDTO;
import com.project.gymweb.dto.create.UserRegisterDTO;
import com.project.gymweb.dto.view.UserRO;
import com.project.gymweb.entities.User;
import com.project.gymweb.security.JwtUtil;
import com.project.gymweb.services.LogoutService;
import com.project.gymweb.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final LogoutService logoutService;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService, LogoutService logoutService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.logoutService = logoutService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserRO> register(@RequestBody UserRegisterDTO userRegisterDTO) {
        var user = userService.createUser(userRegisterDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginDTO userLoginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDTO.username(), userLoginDTO.password())
        );
        User user = userService.findByUsername(userLoginDTO.username());
        return jwtUtil.generateToken(user.getUsername());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        logoutService.logout(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/token/status")
    public ResponseEntity<String> checkTokenStatus(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (logoutService.isTokenBlacklisted(token)) {
            return ResponseEntity.status(HttpStatus.OK).body("Token está na blacklist (inválido).");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Token está válido.");
        }
    }

    @DeleteMapping("/token")
    public ResponseEntity<Void> removeToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        logoutService.removeTokenFromBlacklist(token);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
