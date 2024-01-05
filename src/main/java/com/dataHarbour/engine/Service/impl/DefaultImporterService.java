package com.dataHarbour.engine.Service.impl;

import com.dataHarbour.engine.Configuration.DataHarbourConfiguration;
import com.dataHarbour.engine.Service.DataBaseHandler;
import com.dataHarbour.engine.Service.ImporterService;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import com.dataHarbour.engine.model.DataConnectionModel;
import com.dataHarbour.engine.model.UpdateOperationModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DefaultImporterService implements ImporterService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultImporterService.class);

    private static final String INSERT_STATEMENT = "INSERT INTO ";
    private static final String REMOVE_STATEMENT = "DELETE FROM %s WHERE ";
    private static final String UPDATE_STATEMENT = "UPDATE %s SET %s WHERE %s";
    private static final String STATEMENT_SEPARATOR = " ,";
    private static final String UNIQUE = "unique";
    private static final String DEFAULT = "default";

    @Autowired
    private DataHarbourConfiguration dataHarbourConfiguration;

    @Autowired
    private DataBaseHandler dataBaseHandler;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ApplicationContext context;

    @Override
    public int createEntry(final List<String> content) {
        if(CollectionUtils.isEmpty(content)){
            return -1;
        }
        String dataBaseId = (String) sessionService.getAttribute(DataHarbourConstants.DATA_BASE_ID);
        DataConnectionModel connectionModel = getDataConnectionModel(dataBaseId);

        if(connectionModel != null)
        {
            String[] headerAttributes = content.get(0).split(dataHarbourConfiguration.getDelimiter());
            StringBuilder builder = new StringBuilder(INSERT_STATEMENT + headerAttributes[1] + "(");

            for(int index = 2; index < headerAttributes.length; index++){
                builder.append(headerAttributes[index]);

                if(index != headerAttributes.length - 1){
                    builder.append(STATEMENT_SEPARATOR);
                }else{
                    builder.append(") VALUES ");
                }
            }

            for(int index = 1 ; index < content.size(); index++){

                String statement =  builder + "('" + content.get(index).replace(dataHarbourConfiguration.getDelimiter(), DataHarbourConstants.VALUE_DELIMITER) + "')";
                LOG.info(String.format("Executing Insert Statement : %s " , statement));
                dataBaseHandler.runStatement(statement, connectionModel.getJdbcTemplate());
            }
        }

        return 0;

    }

    @Override
    public int updateEntry(final List<String> content) {
        if(CollectionUtils.isEmpty(content)){
            return -1;
        }

        String CONDITION_PATTERN = "%s = '%s' AND";
        String SETTER_PATTERN = "%s = '%s' , ";

        String dataBaseId = (String) sessionService.getAttribute(DataHarbourConstants.DATA_BASE_ID);
        DataConnectionModel connectionModel = getDataConnectionModel(dataBaseId);

        if(connectionModel != null){
            String[] headerAttributes = content.get(0).split(dataHarbourConfiguration.getDelimiter());
            String tableName = headerAttributes[1];
            List<UpdateOperationModel> attributes = new ArrayList<>();

            // Mapping Default Values
            for(int headerIndex = 2 ; headerIndex < headerAttributes.length; headerIndex++){
                attributes.add(new UpdateOperationModel(headerAttributes[headerIndex]));
            }

            // Creating Update Statement one by one
            for(int index = 1 ; index < content.size(); index++){
                StringBuilder settingValue = new StringBuilder();
                StringBuilder conditionValue = new StringBuilder();

                String[] lineValues = content.get(index).split(dataHarbourConfiguration.getDelimiter());

                for(int lineIndex = 0 ; lineIndex < lineValues.length; lineIndex++) {
                    UpdateOperationModel updateOperationModel = attributes.get(lineIndex);
                    if(updateOperationModel.isUnique()){
                        if(StringUtils.isEmpty(lineValues[lineIndex])){
                            conditionValue.append(String.format(CONDITION_PATTERN, updateOperationModel.getAttributeName(), updateOperationModel.getDefaultValue()));
                        }else{
                            conditionValue.append(String.format(CONDITION_PATTERN, updateOperationModel.getAttributeName(), lineValues[lineIndex]));
                        }
                    }else{
                        if(StringUtils.isEmpty(lineValues[lineIndex])){
                            settingValue.append(String.format(SETTER_PATTERN, updateOperationModel.getAttributeName(), updateOperationModel.getDefaultValue()));
                        }else{
                            settingValue.append(String.format(SETTER_PATTERN, updateOperationModel.getAttributeName(), lineValues[lineIndex]));
                        }
                    }
                }

                String setValue =  StringUtils.removeEnd(settingValue.toString(), ", ");
                String condValue = StringUtils.removeEnd(conditionValue.toString(), " AND");

                String statement = String.format(UPDATE_STATEMENT, tableName, setValue, condValue);

                LOG.info("Executing update statement : " + statement);
                dataBaseHandler.runStatement(statement, connectionModel.getJdbcTemplate());
            }

        }

        return 0;
    }

    @Override
    public int removeEntry(List<String> content) {
        if(CollectionUtils.isEmpty(content)){
            return -1;
        }

        String columnToString = "%s = '%s' AND";
        String dataBaseId = (String) sessionService.getAttribute(DataHarbourConstants.DATA_BASE_ID);
        DataConnectionModel connectionModel = getDataConnectionModel(dataBaseId);

        if(connectionModel != null){
            String[] headerAttributes = content.get(0).split(dataHarbourConfiguration.getDelimiter());
            StringBuilder builder = new StringBuilder(String.format(REMOVE_STATEMENT, headerAttributes[1]));

            for(int index = 1 ; index < content.size(); index++){

                String[] lineValue = content.get(index).split(dataHarbourConfiguration.getDelimiter());
                StringBuilder valueBuilder = new StringBuilder();

                for(int headerIndex = 2 ; headerIndex < headerAttributes.length; headerIndex++){
                    valueBuilder.append(String.format(columnToString, headerAttributes[headerIndex] , lineValue[headerIndex - 2]));
                }

                String value = StringUtils.removeEnd(valueBuilder.toString(), " AND");

                String statement =  builder + value;
                LOG.info(String.format("Executing Remove Statement : %s " , statement));
                dataBaseHandler.runStatement(statement, connectionModel.getJdbcTemplate());
            }
        }
        return 0;
    }

    private DataConnectionModel getDataConnectionModel(final String connectionId){

        List<DataConnectionModel> dataConnectionModelList = (List<DataConnectionModel>) context.getBean("dataConnectionsList");

        for(DataConnectionModel connectionModel : dataConnectionModelList){
            if(connectionModel.getId().equals(connectionId)){
                return connectionModel;
            }
        };

        return null;
    }
}
