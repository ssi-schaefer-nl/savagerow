package io.aero.v1.dto;

import java.util.List;

public class TableSchemaDTO {
    private String table;
    private List<ColumnSchemaDTO> columns;

    public String getTable() {
        return table;
    }

    public TableSchemaDTO setTable(String table) {
        this.table = table;
        return this;
    }

    public List<ColumnSchemaDTO> getColumns() {
        return columns;
    }

    public TableSchemaDTO setColumns(List<ColumnSchemaDTO> columns) {
        this.columns = columns;
        return this;
    }
}
