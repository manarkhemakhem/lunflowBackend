package com.example.lunflow;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ValueType {
    String StringValue;
    Integer intValue;
    Boolean boolValue;
    LocalDateTime dateValue;

    public ValueType() {}

    public ValueType(String StringValue) {
        this.StringValue = StringValue;
    }

    public ValueType(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public ValueType(Integer intValue) {
        this.intValue = intValue;
    }

    public ValueType(LocalDateTime dateValue) {
        this.dateValue = dateValue;
    }

    public String getStringValue() {
        return StringValue;
    }

    public void setStringValue(String StringValue) {
        this.StringValue = StringValue;
    }

    public Boolean getBoolValue() {
        return boolValue;
    }

    public void setBoolValue(Boolean boolValue) {
        this.boolValue = boolValue;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public LocalDateTime getDateValue() {
        return dateValue;
    }

    public void setDateValue(LocalDateTime dateValue) {
        this.dateValue = dateValue;
    }
}