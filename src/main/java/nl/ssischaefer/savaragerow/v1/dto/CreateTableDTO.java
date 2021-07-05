package nl.ssischaefer.savaragerow.v1.dto;

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
