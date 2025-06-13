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
        UserResponse userResponse = userMapper.toUserResponse(registeredUser);

        Cookie cookie = new Cookie("accessToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        // cookie.setAttribute("SameSite", "Strict"); TODO: look into CSRF protection
        cookie.setPath("/");
        cookie.setMaxAge(7200); // 2 hours
        response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
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
            UserResponse userResponse = userMapper.toUserResponse(user);

            Cookie cookie = new Cookie("accessToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            // cookie.setAttribute("SameSite", "Strict"); TODO: read more about potential risk for CSRF-attacks
            cookie.setPath("/");
            cookie.setMaxAge(7200); // 2 hours
            response.addCookie(cookie);

            return ResponseEntity.ok(userResponse); // instead of authreponse, we return userResponse directly and store the token in a cookie

    } catch (final BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("accessToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // delete the cookie
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }
}