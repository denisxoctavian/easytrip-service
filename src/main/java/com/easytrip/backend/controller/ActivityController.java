package com.easytrip.backend.controller;

import com.easytrip.backend.model.Activity;
import com.easytrip.backend.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActivityController {

    private final ActivityService activityService;

    @GetMapping()
    public ResponseEntity<List<Activity>> all() {
        return ResponseEntity.ok().body(activityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> findActivityById(@PathVariable("id") Long id) {
        return activityService.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }


}
