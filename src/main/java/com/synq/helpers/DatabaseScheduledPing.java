package com.synq.helpers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatabaseScheduledPing {

    @Autowired
    JdbcTemplate jdbcTemplate;


    // Runs automatically every 3 days (in milliseconds) as long as EC2 is running

    @Scheduled(fixedRate = 259200000)

    public void pingSupabase() {

        try {

            jdbcTemplate.execute("SELECT 1;");

            System.out.println("Supabase Keep-Alive Ping: Success");

        } catch (Exception e) {

            System.err.println("Supabase Keep-Alive Ping: Failed -> " + e.getMessage());

        }

    }

}
