package com.dataHarbour.engine.Controller;

import com.dataHarbour.engine.Service.DataParser;
import com.dataHarbour.engine.Utils.DataHarbourConstants;
import com.dataHarbour.engine.model.ContentRequestModel;
import com.dataHarbour.engine.model.ResponseModel;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/api/v1")
public class BaseController {

    private static final String DATA_FILE = "datafile";

    @Autowired
    private DataParser dataParser;

    @PostMapping(value = "/import/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseModel> uploadTextFile(@RequestParam(value = DATA_FILE , required = false) final MultipartFile multipartFile,
                                        @RequestParam(value = DataHarbourConstants.DATA_BASE_ID, required = true) final String dataBaseId)
            throws MissingServletRequestParameterException, FileUploadException
    {
        if(null == multipartFile || multipartFile.isEmpty()){
            throw new MissingServletRequestParameterException("File not Found", DATA_FILE);
        }
        if(!multipartFile.getOriginalFilename().endsWith(".txt")){
            throw new FileUploadException("File is not of right format. Accepted format .txt");
        }
        ResponseModel response = dataParser.parseTextData(multipartFile);
        if(response == null){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        if(response.getStatus().equals(DataHarbourConstants.FAILED)){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/import/content", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ResponseModel> uploadContent(@RequestBody(required = true) final ContentRequestModel contentRequestModel,
            @RequestParam(value = DataHarbourConstants.DATA_BASE_ID, required = true) final String dataBaseId)
    {
        ResponseModel response = dataParser.parseTextData(contentRequestModel.getContent());
        if(response == null){
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }

        if(response.getStatus().equals(DataHarbourConstants.FAILED)){
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
