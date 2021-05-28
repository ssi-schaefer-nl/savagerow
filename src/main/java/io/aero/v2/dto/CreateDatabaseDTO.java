package io.aero.v2.dto;

import java.util.List;

public class CreateDatabaseDTO {
    private String databaseName;
    private List<CreateTableDTO> tables;

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public List<CreateTableDTO> getTables() {
        return tables;
    }

    public void setTables(List<CreateTableDTO> tables) {
        this.tables = tables;
    }
}
