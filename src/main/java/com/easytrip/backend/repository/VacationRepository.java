package com.easytrip.backend.repository;

import com.easytrip.backend.model.User;
import com.easytrip.backend.model.Vacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VacationRepository extends JpaRepository<Vacation,Long> {

    List<Vacation> findByUser(User user);
}
