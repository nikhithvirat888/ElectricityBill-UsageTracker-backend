package com.anurag.cse.config;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/** Simple in-memory session tokens (users must log in again after a server restart). */
@Component
public class TokenStore {
    private final Map<String, Long> tokens = new ConcurrentHashMap<>();

    public String create(Long userId) {
        String token = UUID.randomUUID().toString();
        tokens.put(token, userId);
        return token;
    }
    public Long get(String token) { return token == null ? null : tokens.get(token); }
    public void remove(String token) { if (token != null) tokens.remove(token); }
}