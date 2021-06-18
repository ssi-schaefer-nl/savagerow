package nl.ssischaefer.savaragerow.v1.sqlite.service;

import nl.ssischaefer.savaragerow.v1.dto.AddColumnDTO;
import nl.ssischaefer.savaragerow.v1.sqlite.metadata.JDBCMetaDataManager;
import nl.ssischaefer.savaragerow.v1.sqlite.preparedstatements.PreparedAddColumnStatementBuilder;
import nl.ssischaefer.savaragerow.v1.sqlite.utils.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v1.service.DataDefinitionService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

public class SQLiteDataDefinitionService implements DataDefinitionService {
    @Autowired
    private JDBCMetaDataManager jdbcManager;

    @Override
    public void deleteColumn(String table, String column) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table) || jdbcManager.notAllColumnsExistInTable(table, Arrays.asList(column))) {
            throw new IllegalArgumentException("Invalid table or column name");
        }
        SQLiteDataSource.getConnection().prepareStatement("ALTER TABLE " + table + " DROP " + column).executeUpdate();
        jdbcManager.updateTableMetaDataForTable(table);
    }

    @Override
    public void addColumn(String table, AddColumnDTO column) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table)) {
            throw new IllegalArgumentException("Invalid table");
        }

        new PreparedAddColumnStatementBuilder().setTable(table).setColumn(column).setConnection(SQLiteDataSource.getConnection()).execute();
        jdbcManager.updateTableMetaDataForTable(table);
    }

    @Override
    public void renameColumn(String table, String column, String newName) throws Exception {
        if (jdbcManager.tableNotExistsInDb(table) || jdbcManager.notAllColumnsExistInTable(table, Arrays.asList(column))) {
            throw new IllegalArgumentException("Invalid table or column name");
        }

        //todo protect against SQL injection
        SQLiteDataSource.getConnection().prepareStatement("ALTER TABLE " + table + " RENAME COLUMN " + column + " TO " + newName).executeUpdate();
        jdbcManager.updateTableMetaDataForTable(table);
    }
}
