package com.dataHarbour.engine.Service.impl;

import com.dataHarbour.engine.Configuration.DataHarbourConfiguration;
import com.dataHarbour.engine.Service.DataParser;
import com.dataHarbour.engine.Service.ImporterService;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import com.dataHarbour.engine.model.ResponseModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DefaultDataParser implements DataParser {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultDataParser.class);

    private static final List<String> OPERATIONS = List.of("$INSERT", "$UPDATE", "$REMOVE" , "$insert", "$update", "$remove");

    @Autowired
    private DataHarbourConfiguration dataHarbourConfiguration;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ImporterService importerService;

    @Autowired
    private Environment environment;

    @Override
    public ResponseModel parseTextData(final MultipartFile multipartFile) {

        try{

            List<String> fileContent = parseIncomingData(multipartFile);
            fileContent = fileContent.stream().filter(StringUtils::isNotEmpty).toList();
            return initiateDataBaseOperation(fileContent);

        }catch (Exception ex){

            LOG.error("Exception occurred while parsing data ", ex);
        }finally {

        }

        return null;

    }

    @Override
    public void parseDataFromExcel() {

    }

    @Override
    public ResponseModel parseTextData(String content) {
        content = content.trim();
        List<String> textToList = Arrays.asList(content.split(StringUtils.LF));
        textToList = textToList.stream().filter(StringUtils::isNotEmpty).toList();
        return initiateDataBaseOperation(textToList);
    }

    private ResponseModel initiateDataBaseOperation(final List<String> fileContent){

        if(CollectionUtils.isNotEmpty(fileContent)){
            ResponseModel mergedResponseModel = new ResponseModel("SUCCESS", new ArrayList<String>());

            int count = 0;
            while(count < fileContent.size()){

                String line = fileContent.get(count);

                if(BooleanUtils.isTrue(isHeaderLine(line))){

                    List<String> content = new ArrayList<>();
                    content.add(line);
                    String operation = "$" + StringUtils.substringBetween(line, "$", environment.getProperty("DataHarbour.data.delimiter"));
                    count++;

                    while(count < fileContent.size()){
                        String dataValue = fileContent.get(count);

                        if(BooleanUtils.isFalse(isHeaderLine(dataValue))){
                            content.add(dataValue);
                            count++;

                        }else{
                            break;
                        }
                    }

                    if(CollectionUtils.isNotEmpty(content)){
                        if(operation.equals("$INSERT") || operation.equals("$insert")){
                            LOG.info(String.format("Started Creating [%s] Entries", content.size() - 1));
                            ResponseModel responseModel = importerService.createEntry(content);
                            mergeResponse(mergedResponseModel, responseModel);
                        }
                        if(operation.equals("$REMOVE") || operation.equals("$remove")){
                            LOG.info(String.format("Started Removing [%s] Entries", content.size() - 1));
                            ResponseModel responseModel = importerService.removeEntry(content);
                            mergeResponse(mergedResponseModel, responseModel);
                        }
                        if(operation.equals("$UPDATE") || operation.equals("$update")){
                            LOG.info(String.format("Started Updating [%s] Entries ", content.size() - 1));
                            ResponseModel responseModel = importerService.updateEntry(content);
                            mergeResponse(mergedResponseModel, responseModel);
                        }
                    }
                }
            }
            return mergedResponseModel;
        }

        return null;
    }


    private List<String> parseIncomingData(MultipartFile multipartFile) throws IOException {
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream()));
        return bufferedReader.lines().toList();
    }

    private boolean isHeaderLine(final String possibleHeader){

        if(StringUtils.isEmpty(possibleHeader)){
            return false;
        }

        for(String ops : OPERATIONS){
            if(possibleHeader.contains(ops)){
                return true;
            }
        }

        return false;
    }

    private void mergeResponse(ResponseModel parent , ResponseModel child){
        if(CollectionUtils.isNotEmpty(child.getErrors())){
            parent.setStatus(DataHarbourConstants.FAILED);
            parent.getErrors().addAll(child.getErrors());
        }
    }
}
