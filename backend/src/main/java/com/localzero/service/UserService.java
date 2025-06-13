package com.localzero.service;


import com.localzero.exception.EmailAlreadyExistsException;
import com.localzero.exception.RoleNotFoundException;
import com.localzero.exception.UserNotFoundException;
import com.localzero.mapper.UserMapper;
import com.localzero.model.Role;
import com.localzero.model.User;
import com.localzero.dto.CreateUserRequest;
import com.localzero.model.enums.RoleName;
import com.localzero.repository.RoleRepository;
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
    private final RoleRepository roleRepository;

    public UserService(PasswordEncoder passwordEncoder, UserMapper userMapper,
                       UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    public User registerUser(CreateUserRequest createUserRequest) {
        if (userRepository.findByEmail(createUserRequest.email()).isPresent()) {
            throw new EmailAlreadyExistsException(createUserRequest.email());
        }
        User user = userMapper.toUser(createUserRequest);
        user.setPasswordHash(passwordEncoder.encode(createUserRequest.password()));

        Role residentRole = roleRepository.findByRoleName(RoleName.RESIDENT)
                .orElseThrow(() -> new RoleNotFoundException("Role RESIDENT not found"));

        user.getRoles().add(residentRole); // assign resident role by default

        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    // should take DTOs as parameters
}
