package com.anurag.cse.controller;

import com.anurag.cse.config.TokenStore;
import com.anurag.cse.dto.*;
import com.anurag.cse.model.User;
import com.anurag.cse.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final TokenStore tokens;

    public AuthController(UserRepository users, PasswordEncoder encoder, TokenStore tokens) {
        this.users = users; this.encoder = encoder; this.tokens = tokens;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest r) {
        String email = r.getEmail().trim().toLowerCase();
        if (users.existsByEmail(email)) throw new IllegalArgumentException("An account with this email already exists");
        User u = new User();
        u.setName(r.getName().trim());
        u.setEmail(email);
        u.setPassword(encoder.encode(r.getPassword()));
        u.setConsumerNumber(r.getConsumerNumber().trim());
        u.setAddress(r.getAddress());
        users.save(u);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Registration successful. Please log in."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest r) {
        Optional<User> found = users.findByEmail(r.getEmail().trim().toLowerCase());
        if (found.isEmpty() || !encoder.matches(r.getPassword(), found.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Incorrect email or password"));
        }
        User u = found.get();
        return ResponseEntity.ok(Map.of("token", tokens.create(u.getId()), "user", u));
    }

    @PostMapping("/logout")
    public Map<String, String> logout(@RequestHeader(value = "Authorization", required = false) String header) {
        if (header != null && header.startsWith("Bearer ")) tokens.remove(header.substring(7));
        return Map.of("message", "Logged out");
    }
}