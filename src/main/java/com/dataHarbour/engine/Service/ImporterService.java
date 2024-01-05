package com.dataHarbour.engine.Service;

import java.util.List;

public interface ImporterService {

    int createEntry(List<String> content);

    int updateEntry(List<String> content);

    int removeEntry(List<String> content);
}
