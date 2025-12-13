package com.hahn.backend.services;

import com.hahn.backend.dto.responce.UserDto;
import com.hahn.backend.entities.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    // Admin/Internal use
    void createUser(UserDto userDto);

    void updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    List<User> findAllUsers();

    Optional<User> findByEmail(String email);
}