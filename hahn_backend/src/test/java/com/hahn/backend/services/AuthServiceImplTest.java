package com.hahn.backend.services;

import com.hahn.backend.dto.request.LoginRequest;
import com.hahn.backend.dto.request.RegisterRequest;
import com.hahn.backend.dto.response.AuthResponse;
import com.hahn.backend.dto.response.UserDto;
import com.hahn.backend.entities.User;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails; // <--- Import this
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private EntityMapper mapper;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_ShouldReturnToken_WhenUserIsNew() {
        // 1. Arrange
        RegisterRequest request = new RegisterRequest("salah", "password123", "salah@test.com");
        User savedUser = User.builder().id(1L).email("salah@test.com").build();
        UserDto userDto = UserDto.builder().id(1L).email("salah@test.com").build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // FIX: Match any UserDetails object, not String
        when(jwtService.generateToken(any(UserDetails.class))).thenReturn("jwt_token_123");

        when(mapper.toUserDto(savedUser)).thenReturn(userDto);

        // 2. Act
        AuthResponse response = authService.register(request);

        // 3. Assert
        assertNotNull(response);
        assertEquals("jwt_token_123", response.getToken());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        // 1. Arrange
        LoginRequest request = new LoginRequest("salah@test.com", "password123");

        // FIX: Add .password("encodedPass") here!
        User user = User.builder()
                .id(1L)
                .email("salah@test.com")
                .password("encodedPass")
                .build();

        UserDto userDto = UserDto.builder().id(1L).email("salah@test.com").build();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        UserDetails userDetails = authService.mapToUserDetails(user) ;
        when(jwtService.generateToken(userDetails)).thenReturn("jwt_token_456"); // or pass user if changed
        when(mapper.toUserDto(user)).thenReturn(userDto);

        // 2. Act
        AuthResponse response = authService.login(request);

        // 3. Assert
        assertNotNull(response);
        assertEquals("jwt_token_456", response.getToken());
        verify(authenticationManager).authenticate(any());
    }
}