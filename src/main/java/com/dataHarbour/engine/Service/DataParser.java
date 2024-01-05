package com.dataHarbour.engine.Service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataParser {

    void parseTextData(MultipartFile multipartFile);

    void parseDataFromExcel();

    void parseTextData(String content);
}
