package com.anurag.cse.controller;

import com.anurag.cse.dto.ProfileRequest;
import com.anurag.cse.model.User;
import com.anurag.cse.repository.UserRepository;
import jakarta.validation.Valid;
import java.util.NoSuchElementException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {
    private final UserRepository users;
    private final PasswordEncoder encoder;

    public ProfileController(UserRepository users, PasswordEncoder encoder) { this.users = users; this.encoder = encoder; }

    @GetMapping
    public User get(@RequestAttribute("userId") Long uid) {
        return users.findById(uid).orElseThrow(() -> new NoSuchElementException("User not found"));
    }

    @PutMapping
    public User update(@RequestAttribute("userId") Long uid, @Valid @RequestBody ProfileRequest r) {
        User u = users.findById(uid).orElseThrow(() -> new NoSuchElementException("User not found"));
        u.setName(r.getName().trim());
        u.setConsumerNumber(r.getConsumerNumber().trim());
        u.setAddress(r.getAddress());
        if (r.getNewPassword() != null && !r.getNewPassword().isBlank()) {
            if (r.getNewPassword().length() < 6) throw new IllegalArgumentException("Password must be at least 6 characters");
            u.setPassword(encoder.encode(r.getNewPassword()));
        }
        return users.save(u);
    }
}