package com.dataHarbour.engine.Service;

import com.dataHarbour.engine.model.ResponseModel;
import org.springframework.web.multipart.MultipartFile;

public interface DataParser {

    ResponseModel parseTextData(MultipartFile multipartFile);

    void parseDataFromExcel();

    ResponseModel parseTextData(String content);
}
