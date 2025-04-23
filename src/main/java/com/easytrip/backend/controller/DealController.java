package com.easytrip.backend.controller;

import com.easytrip.backend.model.Deal;
import com.easytrip.backend.service.DealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deals")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @GetMapping("/active")
    public List<Deal> getActive() {
        return dealService.getActiveDeals();
    }
}
