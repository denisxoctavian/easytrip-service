package com.easytrip.backend.service;

import com.easytrip.backend.model.Deal;
import com.easytrip.backend.repository.DealRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final DealRepository dealRepository;

    public List<Deal> getActiveDeals() {
        return dealRepository.findByActiveTrue();
    }
}
