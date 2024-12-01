package com.majlo.antares.controller;

import com.majlo.antares.config.UserAuthenticationProvider;
import com.majlo.antares.dtos.CredentialsDto;
import com.majlo.antares.dtos.SignUpDto;
import com.majlo.antares.dtos.UserDto;
import com.majlo.antares.model.User;
import com.majlo.antares.service.AuthorizationService;
import com.majlo.antares.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthenticationProvider userAuthenticationProvider;
    private final AuthorizationService authorizationService;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody @Valid CredentialsDto credentialsDto) {
        UserDto userDto = userService.login(credentialsDto);
        userDto.setToken(userAuthenticationProvider.createToken(userDto));
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody @Valid SignUpDto user) {
        user.printSignUpDto();
        UserDto createdUser = userService.register(user);
        createdUser.setToken(userAuthenticationProvider.createToken(createdUser));
        return ResponseEntity.created(URI.create("/users/" + createdUser.getId())).body(createdUser);
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRole(@RequestHeader("Authorization") String authHeader) {

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            User user = authorizationService.getAuthenticatedUser(authHeader);

            return ResponseEntity.ok(user.getRole().toString());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not logged in");
    }

}
