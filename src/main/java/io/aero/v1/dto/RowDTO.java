package io.aero.v1.dto;

import java.util.Map;

public class RowDTO {
    Map<String, String> row;

    public Map<String, String> getRow() {
        return row;
    }

    public RowDTO setRow(Map<String, String> newRow) {
        this.row = newRow;
        return this;
    }

    @Override
    public String toString() {
        return "RowDTO{" +
                "row=" + row +
                '}';
    }
}
