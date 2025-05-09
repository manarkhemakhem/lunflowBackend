package com.example.lunflow;

import java.util.List;

public class FilterRequest {
    private List<Filter> filters;

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

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public void setFiltrageFields(List<FilterField> filtrageFields) {
        this.filtrageFields = filtrageFields;
    }


}
