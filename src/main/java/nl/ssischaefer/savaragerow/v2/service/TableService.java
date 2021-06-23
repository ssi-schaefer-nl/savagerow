package nl.ssischaefer.savaragerow.v2.service;

import nl.ssischaefer.savaragerow.v2.dto.SQLColumn;
import nl.ssischaefer.savaragerow.v2.query.ddl.CreateTableQuery;
import nl.ssischaefer.savaragerow.v2.query.ddl.DeleteTableQuery;
import nl.ssischaefer.savaragerow.v2.query.ddl.RenameTableQuery;
import nl.ssischaefer.savaragerow.v2.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.query.metadata.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableService {

    public void recreateTable(String table, List<SQLColumn> columns) throws Exception {
        List<String> currentColumns = new GetColumnsQuery().setTable(table).execute().stream().map(SQLColumn::getName).collect(Collectors.toList());
        List<String> selectColumns = currentColumns.stream().filter(c -> columns.stream().anyMatch(x -> x.getName().equals(c))).collect(Collectors.toList());

        new CreateTableQuery().setTableName(table+"_temp").setColumns(columns).execute();
        new InsertRowQuery().setFromTable(table).setToTable(table+"_temp").setSelectColumns(selectColumns).execute();
        new DeleteTableQuery().setTable(table).execute();
        new RenameTableQuery().setFrom(table+"_temp").setTo(table).execute();
    }

    public List<SQLColumn> getColumnsForTable(String table) throws SQLException {
        List<SQLColumn> columns = new GetColumnsQuery().setTable(table).execute();
        List<String> pkColumnNames = new GetPrimaryKeyColumnsQuery().setTable(table).execute();
        Map<String, String> fkColumnNames = new GetForeignKeysQuery().setTable(table).execute();
        columns.stream().filter(c -> pkColumnNames.contains(c.getName())).forEach(c -> c.setPk(true));
        columns.stream().filter(c -> fkColumnNames.containsKey(c.getName())).forEach(c -> c.setFk(fkColumnNames.getOrDefault(c.getName(), null)));
        return columns;
    }
}
