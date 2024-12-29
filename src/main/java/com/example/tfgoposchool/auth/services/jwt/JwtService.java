package com.example.tfgoposchool.auth.services.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token);

    String generateToken(UserDetails userDetails);

    boolean tokenValid(String token, UserDetails userDetails);
}
