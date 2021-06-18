package io.aero.v1.dto;

import java.util.List;
import java.util.Map;

public class RowSetDTO {
    List<Map<String, String>> rows;

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public RowSetDTO setRows(List<Map<String, String>> rows) {
        this.rows = rows;
        return this;
    }
}
