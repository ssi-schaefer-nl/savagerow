package nl.ssischaefer.savaragerow.dto;

import java.util.List;

public class TableSchemaDTO {
    private String name;
    private List<SQLColumn> columns;

    public String getName() {
        return name;
    }

    public TableSchemaDTO setName(String name) {
        this.name = name;
        return this;
    }

    public List<SQLColumn> getColumns() {
        return columns;
    }

    public TableSchemaDTO setColumns(List<SQLColumn> columns) {
        this.columns = columns;
        return this;
    }
}
