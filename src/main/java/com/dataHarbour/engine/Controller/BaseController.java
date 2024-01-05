package com.dataHarbour.engine.Controller;

import com.dataHarbour.engine.Service.DataParser;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import com.dataHarbour.engine.model.ContentRequestModel;
import com.dataHarbour.engine.model.DataConnectionModel;
import com.dataHarbour.engine.model.ResponseModel;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/api/v1")
public class BaseController {

    private static final String DATA_FILE = "datafile";

    @Autowired
    private DataParser dataParser;

    @PostMapping(value = "/import/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel uploadTextFile(@RequestParam(value = DATA_FILE , required = false) final MultipartFile multipartFile,
                                        @RequestParam(value = DataHarbourConstants.DATA_BASE_ID, required = true) final String dataBaseId)
            throws MissingServletRequestParameterException, FileUploadException
    {
        if(null == multipartFile || multipartFile.isEmpty()){
            throw new MissingServletRequestParameterException("File not Found", DATA_FILE);
        }
        if(!multipartFile.getOriginalFilename().endsWith(".txt")){
            throw new FileUploadException("File is not of right format. Accepted format .txt");
        }
        dataParser.parseTextData(multipartFile);

        return null;
    }

    @PostMapping(value = "/import/content", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseModel uploadContent(@RequestBody(required = true) final ContentRequestModel contentRequestModel,
            @RequestParam(value = DataHarbourConstants.DATA_BASE_ID, required = true) final String dataBaseId)
    {
        dataParser.parseTextData(contentRequestModel.getContent());
        return null;
    }


}
