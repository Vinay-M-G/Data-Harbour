package com.dataHarbour.engine.model;

import com.dataHarbour.engine.Utils.DataHarbourConstants;
import org.apache.commons.lang3.StringUtils;

public class UpdateOperationModel {

    private static final String UNIQUE = "unique";
    private static final String DEFAULT = "default=";
    private boolean unique;
    private final String defaultValue;

    private final String attributeName;

    public String getDefaultValue(){
        return defaultValue;
    }

    public boolean isUnique(){
        return unique;
    }

    public String getAttributeName(){
        return attributeName;
    }

    public UpdateOperationModel(final String attributeName){
        this.unique = attributeName.contains(UNIQUE);

        if(attributeName.contains(DEFAULT)){
            this.defaultValue = StringUtils.substringBetween(attributeName, DEFAULT, "]");
        }else{
            this.defaultValue = StringUtils.EMPTY;
        }

        if(attributeName.contains("[")){
            this.attributeName = StringUtils.substringBefore(attributeName, "[");
        }else{
            this.attributeName = attributeName;
        }

    }

    @Override
    public String toString(){
        return "[ " + this.attributeName + DataHarbourConstants.COMMA + this.defaultValue + DataHarbourConstants.COMMA + this.unique + " ]" ;
    }

}
