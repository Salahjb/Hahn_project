package com.hahn.backend.services;

import com.hahn.backend.dto.response.UserDto;
import com.hahn.backend.entities.User;
import com.hahn.backend.exceptions.AccessDeniedException;
import com.hahn.backend.exceptions.ResourceNotFoundException;
import com.hahn.backend.repositories.UserRepository;
import com.hahn.backend.util.EntityMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository ;
    private final EntityMapper mapper ;


    @Override
    public UserDto getCurrentUser() {
        User user = getAuthenticatedUser() ;
        return mapper.toUserDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // SECURITY CHECK: Ensure the logged-in user is updating THEIR OWN account
        User currentUser = getAuthenticatedUser();
        if (!currentUser.getId().equals(user.getId())) {
            throw new AccessDeniedException("You are not authorized to update this profile");

        }
        if (userDto.getUsername() != null) {
            user.setUsername(userDto.getUsername());
        }

        User updatedUser = userRepository.save(user);
        return mapper.toUserDto(updatedUser);
    }


    private User  getAuthenticatedUser(){
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication() ;
         if(authentication == null || !authentication.isAuthenticated()){
             throw new RuntimeException("User is not authenticated !") ;
         }
        // We can cast directly because we set it in the Filter
        return (User) authentication.getPrincipal();
    }
}
