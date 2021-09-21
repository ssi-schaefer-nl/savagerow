package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.storage.common.model.SQLColumn;
import nl.ssischaefer.savaragerow.storage.common.sql.SQLiteDatatype;
import nl.ssischaefer.savaragerow.storage.query.*;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageManagementController {

    public void recreateTable(String table, List<SQLColumn> columns) throws Exception {
        List<String> currentColumns = new GetColumnsQuery().setTable(table).execute().stream().map(SQLColumn::getName).collect(Collectors.toList());
        List<String> selectColumns = currentColumns.stream().filter(c -> columns.stream().anyMatch(x -> x.getName().equals(c))).collect(Collectors.toList());

        new CreateTableQuery().setTableName(table+"_temp").setColumns(columns).executeUpdate();
        new InsertRowQuery().setFromTable(table).setToTable(table+"_temp").setSelectColumns(selectColumns).executeUpdate();
        new DeleteTableQuery().setTable(table).executeUpdate();
        new RenameTableQuery().setFrom(table+"_temp").setTo(table).executeQuery();
    }

    public List<SQLColumn> getColumnsForTable(String table) throws SQLException {
        List<SQLColumn> columns = new GetColumnsQuery().setTable(table).execute();
        List<String> pkColumnNames = new GetPrimaryKeyColumnsQuery().setTable(table).execute();
        Map<String, String> fkColumnNames = new GetForeignKeysQuery().setTable(table).execute();
        columns.stream().filter(c -> pkColumnNames.contains(c.getName())).forEach(c -> c.setPk(true));
        columns.stream().filter(c -> fkColumnNames.containsKey(c.getName())).forEach(c -> c.setFk(fkColumnNames.getOrDefault(c.getName(), null)));
        return columns;
    }

    public void deleteTable(String table) throws Exception {
        new DeleteTableQuery().setTable(table).executeUpdate();
    }

    public void renameColumn(String table, String column, String newName) throws Exception {
        new RenameColumnQuery().setTable(table).setFrom(column).setTo(newName).executeQuery();
    }

    public void addTable(String table) throws Exception {
        SQLColumn columnSchema = new SQLColumn().setName("id").setDatatype(SQLiteDatatype.Integer).setNullable(false).setPk(true);
        new CreateTableQuery().setTableName(table).setColumns(Collections.singletonList(columnSchema)).executeUpdate();
    }

    public List<String> getTables() throws SQLException {
        return new GetTablesQuery().execute().getResult();
    }
}
