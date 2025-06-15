package com.localzero.controller;

import com.localzero.mapper.UserMapper;
import com.localzero.model.User;
import com.localzero.dto.CreateUserRequest;
import com.localzero.dto.LoginRequest;
import com.localzero.dto.UserResponse;
import com.localzero.security.JWTService;
import com.localzero.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final String ACCESS_TOKEN_COOKIE_NAME = "accessToken";
    private static final String COOKIE_PATH = "/";
    private static final int COOKIE_MAX_AGE_SECONDS = 7200; // 2 hours

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JWTService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserMapper userMapper, JWTService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
        this.jwtService = jwtService;
    }

    @GetMapping("/me") // if we reach this endpoint, the user is authenticated (because of the JWTAuthenticationFilter)
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        User user = userService.getUserByEmail(email);
        UserResponse userResponse = userMapper.toUserResponse(user);

        return ResponseEntity.ok(userResponse);
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest createUserRequest, HttpServletResponse response) {
        User registeredUser = userService.registerUser(createUserRequest);
        String token = jwtService.createToken(registeredUser.getEmail());
        addTokenCookie(response, token);

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toUserResponse(registeredUser));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.email(),
                            loginRequest.password()
                    )
            );

            User user = userService.getUserByEmail(loginRequest.email());
            String token = jwtService.createToken(user.getEmail());
            addTokenCookie(response, token);

            return ResponseEntity.ok(userMapper.toUserResponse(user));

        } catch (final BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        clearTokenCookie(response);
        return ResponseEntity.ok().build();
    }

    private void addTokenCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);
    }

    private void clearTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_COOKIE_NAME, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath(COOKIE_PATH);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}