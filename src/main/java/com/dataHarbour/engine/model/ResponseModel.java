package com.dataHarbour.engine.model;

import java.util.List;

public class ResponseModel {

    private String status;
    private List<String> errors;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public ResponseModel(final String status, final List<String> errors){
        this.status = status;
        this.errors = errors;
    }



}
