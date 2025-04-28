package com.example.lunflow;

import java.util.List;

public class FilterRequest {
    private boolean isFilterEnabled;  // Indique si le filtrage est activé
    private List<FilterField> filtrageFields;
    public boolean isFilterEnabled() {
        return isFilterEnabled;
    }

    public void setFilterEnabled(boolean filterEnabled) {
        isFilterEnabled = filterEnabled;
    }

    public List<FilterField> getFiltrageFields() {
        return filtrageFields;
    }

    public void setFiltrageFields(List<FilterField> filtrageFields) {
        this.filtrageFields = filtrageFields;
    }

}
