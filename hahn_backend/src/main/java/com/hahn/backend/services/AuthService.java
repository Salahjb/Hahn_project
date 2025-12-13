package com.hahn.backend.services;

import com.hahn.backend.dto.responce.AuthResponse;
import com.hahn.backend.dto.request.LoginRequest;
import com.hahn.backend.dto.request.RegisterRequest;

public interface AuthService {
    // Registers a new user and returns a Token
    AuthResponse register(RegisterRequest request);

    // Verifies credentials and returns a Token
    AuthResponse login(LoginRequest request);
}