package nl.ssischaefer.savagerow.dto;

import java.util.List;
import java.util.Map;

public class TableDataDTO {
    private TableSchemaDTO tableSchema;
    private List<Map<String, String>> data;

    public TableSchemaDTO getTableSchema() {
        return tableSchema;
    }

    public TableDataDTO setTableSchema(TableSchemaDTO tableSchema) {
        this.tableSchema = tableSchema;
        return this;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public TableDataDTO setData(List<Map<String, String>> data) {
        this.data = data;
        return this;
    }
}
