package io.aero.integration.sqlite.service;

import io.aero.dto.RowDTO;
import io.aero.dto.RowUpdateDTO;
import io.aero.integration.sqlite.metadata.JDBCMetaDataManager;
import io.aero.integration.sqlite.preparedstatements.PreparedDeleteStatementBuilder;
import io.aero.integration.sqlite.preparedstatements.PreparedInsertStatementBuilder;
import io.aero.integration.sqlite.preparedstatements.PreparedUpdateStatementBuilder;
import io.aero.integration.sqlite.utils.SQLiteDataSource;
import io.aero.service.DataManipulationService;
import io.aero.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

public class SQLiteDataManipulationService implements DataManipulationService {
    @Autowired
    private JDBCMetaDataManager jdbcManager;
    @Autowired
    QueryService queryService;


    @Override
    public void updateRow(String table, RowUpdateDTO update) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)
                || jdbcManager.notAllColumnsExistInTable(table, new ArrayList<>(update.getOldRow().keySet()))
                || jdbcManager.notAllColumnsExistInTable(table, new ArrayList<>(update.getNewRow().keySet()))) {
            throw new IllegalArgumentException("Invalid table or column names");
        }
        new PreparedUpdateStatementBuilder()
                .setTable(table)
                .setOriginalRow(update.getOldRow())
                .setNewRow(update.getNewRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(table))
                .setConnection(SQLiteDataSource.getConnection())
                .execute();
    }

    @Override
    public RowDTO addRow(String table, RowDTO newRow) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)
                || jdbcManager.notAllColumnsExistInTable(table, new ArrayList<>(newRow.getRow().keySet()))) {
            throw new IllegalArgumentException("Invalid table or column names");
        }

        int rowId = new PreparedInsertStatementBuilder()
                .setTable(table)
                .setNewRow(newRow.getRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(table))
                .setConnection(SQLiteDataSource.getConnection())
                .execute();

        return queryService.find(table, rowId);
    }

    @Override
    public void deleteRow(String tableName, RowDTO row) throws Exception {
        if (jdbcManager.tableNotExistsInDb(tableName)
                || jdbcManager.notAllColumnsExistInTable(tableName, new ArrayList<>(row.getRow().keySet()))) {
            throw new IllegalArgumentException("Invalid table or column names");
        }

        new PreparedDeleteStatementBuilder()
                .setTable(tableName)
                .setRow(row.getRow())
                .setColumnMetaData(jdbcManager.getColumnsForTable(tableName))
                .setConnection(SQLiteDataSource.getConnection())
                .execute();
    }


}
