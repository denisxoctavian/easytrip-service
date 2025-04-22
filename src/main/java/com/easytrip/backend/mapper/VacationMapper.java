package com.easytrip.backend.mapper;

import com.easytrip.backend.dto.VacationWithItineraryDto;
import com.easytrip.backend.model.Vacation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VacationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "activities", target = "activityList")
    @Mapping(source = "itinerary", target = "itineraryDayList")
    VacationWithItineraryDto toDto(Vacation vacation);
}
