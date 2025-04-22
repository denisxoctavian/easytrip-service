package com.easytrip.backend.service;

import com.easytrip.backend.dto.VacationDto;
import com.easytrip.backend.model.ItineraryDay;
import com.easytrip.backend.model.User;
import com.easytrip.backend.model.Vacation;
import com.easytrip.backend.repository.VacationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VacationService {

    private final VacationRepository vacationRepository;
    private final ItineraryDayService itineraryDayService;

    public List<Vacation> findAll(){
        return vacationRepository.findAll();
    }

    public Vacation findById(Long id) {
        return vacationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacation not found with id: " + id));
    }

    public List<Vacation> findByUser(User user){
        return vacationRepository.findByUser(user);
    }

    public Vacation createVacation(VacationDto vacationDto, User user) throws JsonProcessingException {

        Vacation vacation = new Vacation();
        vacation.setDays(vacationDto.getDays());
        vacation.setBudget(vacationDto.getBudget());
        vacation.setUser(user);
        vacation.setCountry(vacationDto.getCountry());
        vacation.setStartingDate(vacationDto.getStartingDate());
        vacation.setEndingDate(vacationDto.getEndingDate());
        vacation.setTravelCompanion(vacationDto.getTravelCompanion());
        vacation.setActivities(vacationDto.getActivityList());

        vacationRepository.save(vacation);

        List<ItineraryDay> itineraryDays = itineraryDayService.generateItineraryDays(vacation);

        vacation.setItinerary(itineraryDays);

        vacationRepository.save(vacation);

        return vacation;
    }

}
