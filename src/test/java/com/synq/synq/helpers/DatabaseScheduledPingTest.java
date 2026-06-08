package com.synq.synq.helpers;

import com.synq.helpers.DatabaseScheduledPing;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
@SpringBootTest
public class DatabaseScheduledPingTest {

    @Autowired
    private DatabaseScheduledPing databaseScheduledPing;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testPingSupabase_Success() {

        databaseScheduledPing.pingSupabase();

        verify(jdbcTemplate, times(1)).execute("SELECT 1;");
    }

    @Test
    public void testPingSupabase_HandlesException() {
        Mockito.doThrow(new RuntimeException("Database Connection Timeout"))
                .when(jdbcTemplate).execute("SELECT 1;");

        databaseScheduledPing.pingSupabase();
        verify(jdbcTemplate, times(1)).execute("SELECT 1;");
    }
}
