package com.dataHarbour.engine.model;

import org.springframework.jdbc.core.JdbcTemplate;

public class DataConnectionModel {

    private String id;
    private JdbcTemplate jdbcTemplate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
