package io.aero.dto;

import java.util.Map;

public class RowInsertDTO {
    Map<String, String> newRow;

    public Map<String, String> getNewRow() {
        return newRow;
    }

    public RowInsertDTO setNewRow(Map<String, String> newRow) {
        this.newRow = newRow;
        return this;
    }
}
