package com.easytrip.backend.dto;


import com.easytrip.backend.enums.TravelCompanion;
import com.easytrip.backend.model.Activity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VacationDto {

    Long id;
    BigDecimal budget;
    String country;
    Integer days;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate startingDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate endingDate;
    TravelCompanion travelCompanion;
    List<Activity> activityList;
}
