package com.dataHarbour.engine.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public interface DataBaseHandler {

    int runStatement(String Statement, JdbcTemplate jdbcTemplate);
}
