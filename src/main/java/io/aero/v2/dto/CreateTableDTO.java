package io.aero.v2.dto;

import java.util.List;

public class CreateTableDTO {
    private String tableName;
    private List<ColumnSchemaDTO> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ColumnSchemaDTO> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnSchemaDTO> columns) {
        this.columns = columns;
    }
}
