package com.easytrip.backend.service;


import com.easytrip.backend.dto.CredentialsDto;
import com.easytrip.backend.dto.SignUpDto;
import com.easytrip.backend.dto.UserDto;
import com.easytrip.backend.exception.AppException;
import com.easytrip.backend.mapper.UserMapper;
import com.easytrip.backend.model.User;
import com.easytrip.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.CharBuffer;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;



    public UserDto login(CredentialsDto credentialsDto){

        User user = userRepository.findByEmail(credentialsDto.email())
                .orElseThrow(()-> new AppException("Unknown user", HttpStatus.NOT_FOUND));

        if(passwordEncoder.matches(CharBuffer.wrap(credentialsDto.password()),
                user.getPassword())){
            return userMapper.toUserDto(user);
        }
        throw new AppException("Invalid password",HttpStatus.BAD_REQUEST);

    }

    public UserDto register(SignUpDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

        if (optionalUser.isPresent()) {
            throw new AppException("User already exists", HttpStatus.BAD_REQUEST);
        }

        User user = userMapper.signUpToUser(userDto);
        user.setPassword(passwordEncoder.encode(CharBuffer.wrap(userDto.password())));

        User savedUser = userRepository.save(user);

        return userMapper.toUserDto(savedUser);
    }

    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException("Unknown user", HttpStatus.NOT_FOUND));
        return userMapper.toUserDto(user);
    }
}
