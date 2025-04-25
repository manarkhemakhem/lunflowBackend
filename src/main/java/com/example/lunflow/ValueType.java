package com.example.lunflow;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class ValueType {
    String stringValue;
    Integer intValue;
    Boolean boolValue;
    LocalDateTime dateValue;
    private Instant instantValue;


    public ValueType() {}

    public ValueType(String StringValue) {
        this.stringValue = StringValue;
    }

    public Instant getInstantValue() {
        return instantValue;
    }

    public void setInstantValue(Instant instantValue) {
        this.instantValue = instantValue;
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
        return stringValue;
    }

    public void setStringValue(String StringValue) {
        this.stringValue = StringValue;
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
    @Override
    public String toString() {
        return "ValueType{" +
                "StringValue='" + stringValue + '\'' +
                ", intValue=" + intValue +
                ", boolValue=" + boolValue +
                ", dateValue=" + dateValue +
                ", instantValue=" + instantValue +
                '}';
    }
}