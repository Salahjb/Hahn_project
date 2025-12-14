package com.hahn.backend.services;

import com.hahn.backend.dto.response.AuthResponse;
import com.hahn.backend.dto.request.LoginRequest;
import com.hahn.backend.dto.request.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);


    AuthResponse login(LoginRequest request);
}