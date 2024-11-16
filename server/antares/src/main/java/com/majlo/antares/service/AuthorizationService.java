package com.majlo.antares.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.model.User;
import com.majlo.antares.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthorizationService {
    private final UserRepository userRepository;
    UserAuthenticationProvider userAuthenticationProvider;

    public AuthorizationService(UserAuthenticationProvider userAuthenticationProvider, UserRepository userRepository) {
        this.userAuthenticationProvider = userAuthenticationProvider;
        this.userRepository = userRepository;
    }

    public Long getAuthenticatedUserId(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                Authentication authentication = userAuthenticationProvider.validateToken(token);
                Long userId = ((UserDto) authentication.getPrincipal()).getId();
                return userId;
            }
            catch (JWTVerificationException e) {
                return null;
            }
        }

        return null;
    }

    public User getAuthenticatedUser(String authHeader) {
        Long userId = getAuthenticatedUserId(authHeader);
        if (userId != null) {
            return userRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        }

        return null;
    }


}
