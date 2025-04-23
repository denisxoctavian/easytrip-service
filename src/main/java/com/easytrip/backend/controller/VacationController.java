package com.easytrip.backend.controller;

import com.easytrip.backend.dto.UserDto;
import com.easytrip.backend.dto.VacationDto;
import com.easytrip.backend.dto.VacationWithItineraryDto;
import com.easytrip.backend.mapper.UserMapper;
import com.easytrip.backend.mapper.VacationMapper;
import com.easytrip.backend.model.User;
import com.easytrip.backend.model.Vacation;
import com.easytrip.backend.service.UserService;
import com.easytrip.backend.service.VacationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/vacations")
public class VacationController {

    private final VacationService vacationService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final VacationMapper vacationMapper;

    @GetMapping()
    public ResponseEntity<List<VacationWithItineraryDto>> all() {
        List<VacationWithItineraryDto> vacationDtos = vacationService.findAll()
                .stream()
                .map(vacationMapper::toDto)
                .toList();

        return ResponseEntity.ok(vacationDtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<VacationWithItineraryDto> findVacationById(@PathVariable("id") Long id) {
        VacationWithItineraryDto vacationWithItineraryDto = vacationMapper.toDto(vacationService.findById(id));
        return ResponseEntity.ok(vacationWithItineraryDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVacationById(@PathVariable("id") Long id){
        Vacation existingVacation = this.vacationService.findById(id);
        if(existingVacation != null){
            vacationService.deleteById(existingVacation);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping()
    public ResponseEntity<Vacation> create(@RequestBody VacationDto vacationDto,
                                              @AuthenticationPrincipal UserDto userDto) throws JsonProcessingException {
        User user =userMapper.toUser(
                userService.findByEmail(userDto.getEmail()));

        Vacation vacation = vacationService.createVacation(vacationDto,user);

        return ResponseEntity.ok(vacation);
    }

}
