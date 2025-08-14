package com.shervinee.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class HealthApiIT extends AbstractIntegrationTest{

    @Autowired
    TestRestTemplate restTemplate;

    @Test
    void healthReturnsOk() {
        var response = restTemplate.withBasicAuth("admin", "admin")
            .getForEntity("http://localhost:8080/api/health", Map.class);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("status")).isEqualTo("ok");
    }
}
