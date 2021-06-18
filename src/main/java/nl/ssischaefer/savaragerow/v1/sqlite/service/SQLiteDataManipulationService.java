package nl.ssischaefer.savaragerow.v1.sqlite.service;

import nl.ssischaefer.savaragerow.v1.dto.RowDTO;
import nl.ssischaefer.savaragerow.v1.dto.RowUpdateDTO;
import nl.ssischaefer.savaragerow.v1.sqlite.metadata.JDBCMetaDataManager;
import nl.ssischaefer.savaragerow.v1.sqlite.preparedstatements.PreparedDeleteStatementBuilder;
import nl.ssischaefer.savaragerow.v1.sqlite.preparedstatements.PreparedInsertStatementBuilder;
import nl.ssischaefer.savaragerow.v1.sqlite.preparedstatements.PreparedUpdateStatementBuilder;
import nl.ssischaefer.savaragerow.v1.sqlite.utils.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v1.service.DataManipulationService;
import nl.ssischaefer.savaragerow.v1.service.QueryService;
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
