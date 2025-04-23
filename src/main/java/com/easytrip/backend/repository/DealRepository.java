package com.easytrip.backend.repository;

import com.easytrip.backend.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DealRepository extends JpaRepository<Deal,Long> {

    List<Deal> findByActiveTrue();
}
