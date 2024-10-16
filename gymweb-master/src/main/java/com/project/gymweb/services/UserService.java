package com.project.gymweb.services;

import com.project.gymweb.dto.create.UserRegisterDTO;
import com.project.gymweb.dto.view.UserRO;
import com.project.gymweb.entities.User;
import com.project.gymweb.exceptions.UserNotFoundException;
import com.project.gymweb.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository repository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserRO> findAll() {
        return repository.findAll().stream().map(this::entityToRO).toList();
    }

    public UserRO findById(UUID id) {
        var user = repository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));
        return entityToRO(user);
    }

    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " was not found"));
    }

    public UserRO findByUsernameRO(String username) {
        var user = repository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username " + username + " was not found"));
        return entityToRO(user);
    }

    public UserRO findByEmailRO(String email) {
        var user = repository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User with email " + email + " was not found"));
        return entityToRO(user);
    }


    public UserRO createUser(UserRegisterDTO userRegisterDTO) {
        var user = dtoToEntity(userRegisterDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var savedUser = repository.save(user);
        return entityToRO(savedUser);
    }

    public UserRO updateUser(UUID id, UserRegisterDTO userRegisterDTO) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));

        user.setUsername(userRegisterDTO.username());
        user.setPassword(passwordEncoder.encode(userRegisterDTO.password()));
        user.setEmail(userRegisterDTO.email());

        var savedUser = repository.save(user);

        return entityToRO(savedUser);
    }

    public void deleteUser(UUID id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " was not found"));
        userRepository.deleteById(user.getId());
    }

    private User dtoToEntity(UserRegisterDTO userRegisterDTO) {
        User user = new User();

        user.setId(UUID.randomUUID());
        user.setUsername(userRegisterDTO.username());
        user.setEmail(userRegisterDTO.email());
        user.setPassword(userRegisterDTO.password());

        return user;
    }

    private UserRO entityToRO(User user) {
        return new UserRO(user.getId(), user.getUsername(), user.getEmail());
    }
}
