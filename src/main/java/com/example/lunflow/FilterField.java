package com.example.lunflow;

public class FilterField {
    private String field;     // Le nom du champ à filtrer
    private String operator;  // L'opérateur pour le filtrage
    private String value;     // La valeur associée au filtrage

    // Getters et setters
    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
