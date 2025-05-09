package com.example.lunflow;

import lombok.Data;

import java.util.List;
@Data
public class Filter {
    private String field;
    private String operator;
    private List<String> value;


}
