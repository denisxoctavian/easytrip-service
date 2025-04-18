package com.easytrip.backend.repository;

import com.easytrip.backend.model.ItineraryDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItineraryDayRepository extends JpaRepository<ItineraryDay,Long> {
}
