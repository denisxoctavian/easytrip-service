package com.easytrip.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/properties")
public class PropertiesController {

    @Value("${map.apiKey}")
    private String mapKey;

    @Value("${rapid.apiKey}")
    private String rapidKey;

    @GetMapping("/map/key")
    public ResponseEntity<Map<String, String>> retrieveMapKey() {
        Map<String, String> response = new HashMap<>();
        response.put("key", mapKey);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rapid/key")
    public ResponseEntity<Map<String, String>> retrieveRapidKey() {
        Map<String, String> response = new HashMap<>();
        response.put("key", rapidKey);
        return ResponseEntity.ok(response);
    }

}
