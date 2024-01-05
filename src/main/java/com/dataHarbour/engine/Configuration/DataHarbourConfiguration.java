package com.dataHarbour.engine.Configuration;

import com.dataHarbour.engine.model.DataConnectionModel;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@PropertySource("dataHarbourApplication.properties")
public class DataHarbourConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DataHarbourConfiguration.class);

    @Autowired
    private Environment environment;

    @Bean(name = "dataConnectionsList")
    public List<DataConnectionModel> createDataConnections(){
        String dataBaseList = environment.getProperty("DataHarbour.availableInstances");
        if(StringUtils.isNotEmpty(dataBaseList)){
            List<DataConnectionModel> dataConnectionModelList = new ArrayList<>();

            String[] dataBaseArray = dataBaseList.split(DataHarbourConstants.COMMA);
            for(String value : dataBaseArray)
            {
                String driverClassName = environment.getProperty(String.format("%s.%s.%s", DataHarbourConstants.DATA_HARBOUR, value, DataHarbourConstants.DRIVER_CLASS_NAME));
                String url = environment.getProperty(String.format("%s.%s.%s", DataHarbourConstants.DATA_HARBOUR, value, DataHarbourConstants.URL));
                String userName = environment.getProperty(String.format("%s.%s.%s", DataHarbourConstants.DATA_HARBOUR, value, DataHarbourConstants.USERNAME));
                String passWord = environment.getProperty(String.format("%s.%s.%s", DataHarbourConstants.DATA_HARBOUR, value, DataHarbourConstants.PASSWORD));

                if(StringUtils.isEmpty(driverClassName) || StringUtils.isEmpty(url) ||
                        StringUtils.isEmpty(userName) || StringUtils.isEmpty(passWord)){

                    LOG.warn(String.format("One or more Required Attributes not available for DB %s", value));
                    continue;
                }

                DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
                driverManagerDataSource.setDriverClassName(driverClassName);
                driverManagerDataSource.setUrl(url);
                driverManagerDataSource.setPassword(passWord);
                driverManagerDataSource.setUsername(userName);

                JdbcTemplate jdbcTemplate = new JdbcTemplate();
                jdbcTemplate.setDataSource(driverManagerDataSource);

                try{
                   driverManagerDataSource.getConnection();
                }catch (Exception ex) {
                    LOG.error(String.format("An Error Occurred while establishing connectivity with DB %s ", value));
                    continue;
                }

                DataConnectionModel dataConnectionModel = new DataConnectionModel();
                dataConnectionModel.setId(value);
                dataConnectionModel.setJdbcTemplate(jdbcTemplate);

                dataConnectionModelList.add(dataConnectionModel);
            }

            return dataConnectionModelList;
        }
        return Collections.emptyList();
    }

    @Bean(name = "delimiter")
    public String getDelimiter(){
        return environment.getProperty("DataHarbour.data.delimiter");
    }

    @Bean(name = "dataBaseList")
    public List<String> getDataBaseList(){
        String dataBaseList = environment.getProperty("DataHarbour.availableInstances");
        return Arrays.asList(dataBaseList.split(DataHarbourConstants.COMMA));
    }

}
