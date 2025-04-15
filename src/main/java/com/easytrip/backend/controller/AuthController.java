package com.easytrip.backend.controller;


import com.easytrip.backend.configuration.UserAuthProvider;
import com.easytrip.backend.dto.CredentialsDto;
import com.easytrip.backend.dto.SignUpDto;
import com.easytrip.backend.dto.UserDto;
import com.easytrip.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final UserAuthProvider userAuthProvider;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody CredentialsDto credentialDto){
        UserDto user = userService.login(credentialDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody SignUpDto signUpDto){
        UserDto user = userService.register(signUpDto);
        user.setToken(userAuthProvider.createToken(user));
        return ResponseEntity.created(URI.create("/users"+user.getId())).body(user);
    }

    @PostMapping("/validate-token")
    public ResponseEntity<Boolean> validateToken(@RequestBody() String authToken) {
        try {
            if (authToken == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
            }

            if(userAuthProvider.isTokenValid(authToken))
                return ResponseEntity.ok().body(true);
            return ResponseEntity.ok().body(false);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(false);
        }
    }


}
