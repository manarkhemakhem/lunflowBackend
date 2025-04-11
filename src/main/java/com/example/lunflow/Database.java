package com.example.lunflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data

public class Database {

        private String name;
        private String uri;


        public String getName() {
                return name;
        }
        public String getUri() {
                return uri;
        }
}
