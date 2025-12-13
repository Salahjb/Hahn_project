package com.hahn.backend.services;

import com.hahn.backend.dto.responce.UserDto;
import com.hahn.backend.entities.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService{

    @Override
    public void createUser(UserDto userDto) {

    }

    @Override
    public void updateUser(Long id, UserDto userDto) {

    }

    @Override
    public void deleteUser(Long id) {

    }

    @Override
    public List<User> findAllUsers() {
        return List.of();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }
}
