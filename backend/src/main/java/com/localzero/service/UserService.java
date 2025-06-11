package com.localzero.service;


import com.localzero.exception.EmailAlreadyExists;
import com.localzero.mapper.UserMapper;
import com.localzero.model.User;
import com.localzero.dto.CreateUserRequest;
import com.localzero.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Transactional //
@Service
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper,
                       UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public User registerUser(CreateUserRequest createUserRequest) {
        if (userRepository.findByEmail(createUserRequest.email()).isPresent()) {
            throw new EmailAlreadyExists(createUserRequest.email());
        }
        User user = userMapper.toUser(createUserRequest);
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.password()));
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email)); // TODO: = better exception handling
    }

    // should take DTOs as parameters
}
