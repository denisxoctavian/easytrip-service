package com.easytrip.backend.repository;

import com.easytrip.backend.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacationRepository extends JpaRepository<Vacation,Long> {
}
