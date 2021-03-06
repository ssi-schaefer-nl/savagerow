package nl.ssischaefer.savaragerow.api.dto;

import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;

import java.util.List;

public class CreateTableDTO {
    private String tableName;
    private List<SQLColumn> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<SQLColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<SQLColumn> columns) {
        this.columns = columns;
    }
}
