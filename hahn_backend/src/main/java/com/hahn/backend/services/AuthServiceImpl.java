package com.hahn.backend.services;

import com.hahn.backend.dto.response.AuthResponse;
import com.hahn.backend.dto.request.LoginRequest;
import com.hahn.backend.dto.request.RegisterRequest;
import com.hahn.backend.dto.response.UserDto;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.UserAlreadyExistsException;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EntityMapper mapper;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + request.getEmail() + " already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .username(request.getUsername())
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(mapToUserDetails(user));


        return new AuthResponse(token, mapper.toUserDto(savedUser));
    }

    @Override
    @Transactional
    public AuthResponse login(LoginRequest request) {
        //  AUTOMATICALLY throws BadCredentialsException if password is wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String token = jwtService.generateToken(mapToUserDetails(user));

        return new AuthResponse(token, mapper.toUserDto(user));
    }

    public UserDetails mapToUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                new ArrayList<>() // No Roles/Authorities for noe
        );
    }
}