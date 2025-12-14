package com.hahn.backend.services;

import com.hahn.backend.dto.response.UserDto;

public interface UserService {

    UserDto getCurrentUser();

    UserDto updateUser(Long id, UserDto userDto);

}