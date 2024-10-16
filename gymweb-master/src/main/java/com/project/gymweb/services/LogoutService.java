package com.project.gymweb.services;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class LogoutService {
    private final Set<String> tokenBlacklist = new HashSet<>();

    public void logout(String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Token inválido");
        }

        String jwtToken = token.substring(7);

        tokenBlacklist.add(jwtToken);

        System.out.println("Token added to blacklist: " + jwtToken);
    }

    public boolean isTokenBlacklisted(String token) {
        String jwtToken = token.substring(7); // Remover "Bearer "
        return tokenBlacklist.contains(jwtToken);
    }

    public void removeTokenFromBlacklist(String token) {
        String jwtToken = token.substring(7);
        tokenBlacklist.remove(jwtToken);

        System.out.println("Token removido da blacklist: " + jwtToken);
    }
}
