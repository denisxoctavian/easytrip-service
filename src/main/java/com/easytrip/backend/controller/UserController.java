package com.easytrip.backend.controller;


import com.easytrip.backend.dto.UserDto;
import com.easytrip.backend.dto.VacationWithItineraryDto;
import com.easytrip.backend.mapper.UserMapper;
import com.easytrip.backend.mapper.VacationMapper;
import com.easytrip.backend.model.User;
import com.easytrip.backend.model.Vacation;
import com.easytrip.backend.service.UserService;
import com.easytrip.backend.service.VacationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final VacationMapper vacationMapper;
    private final VacationService vacationService;


    @GetMapping("/vacations")
    private ResponseEntity<List<VacationWithItineraryDto>> getVacationsOfUser( @AuthenticationPrincipal UserDto userDto) {
        User user =userMapper.toUser(
                userService.findByEmail(userDto.getEmail()));

        List<VacationWithItineraryDto> vacationDtos = vacationService.findByUser(user)
                .stream()
                .map(vacationMapper::toDto)
                .toList();

        return  ResponseEntity.ok(vacationDtos);
    }
}
