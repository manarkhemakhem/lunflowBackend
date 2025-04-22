package com.example.lunflow;

import lombok.Data;

@Data
public class ValueType {
    String StringValue;
    Integer intValue;
    Boolean boolValue;

    public Integer getIntValue() {
        return intValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public String getStringValue() {
        return StringValue;
    }

}
