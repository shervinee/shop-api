package com.shervinee.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

public class DBSmokeTest extends AbstractIntegrationTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void databaseIsUp() {
        Integer one = jdbcTemplate.queryForObject("select 1", Integer.class);
        assertThat(1).isEqualTo(one);
    }

}
