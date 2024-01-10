package com.dataHarbour.engine.Service;

import com.dataHarbour.engine.model.ResponseModel;

import java.util.List;

public interface ImporterService {

    ResponseModel createEntry(List<String> content);

    ResponseModel updateEntry(List<String> content);

    ResponseModel removeEntry(List<String> content);
}
