package com.dataHarbour.engine.Service.impl;

import com.dataHarbour.engine.Service.DataBaseHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;


@Component
public class DefaultDataBaseHandler implements DataBaseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDataBaseHandler.class);

    @Override
    public int runStatement(final String statement, final JdbcTemplate jdbcTemplate) {
        try{
            Objects.requireNonNull(jdbcTemplate);
            if (StringUtils.isNotEmpty(statement)){
                jdbcTemplate.update(statement);
            }

        }catch (Exception ex){

            LOG.error("Exception occurred while executing statement ", ex);
            return -1;
        }

        return 0;
    }
}
