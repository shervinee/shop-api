package com.shervinee.shop.web;

import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {
    private final BuildProperties buildProperties;

    public HealthController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "ok",
            "timestamp", Instant.now().toString(), // live time
            "version", buildProperties.getVersion()));
    }
}
