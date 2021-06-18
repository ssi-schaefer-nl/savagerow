package io.aero.v2.dto;

import java.util.List;

public class TableSchemaDTO {
    private String name;
    private List<ColumnSchemaDTO> columns;

    public String getName() {
        return name;
    }

    public TableSchemaDTO setName(String name) {
        this.name = name;
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
