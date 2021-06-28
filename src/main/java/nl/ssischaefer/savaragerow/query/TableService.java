package nl.ssischaefer.savaragerow.query;

import nl.ssischaefer.savaragerow.dto.SQLColumn;
import nl.ssischaefer.savaragerow.query.ddl.CreateTableQuery;
import nl.ssischaefer.savaragerow.query.ddl.DeleteTableQuery;
import nl.ssischaefer.savaragerow.query.ddl.RenameTableQuery;
import nl.ssischaefer.savaragerow.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.query.metadata.GetColumnsQuery;
import nl.ssischaefer.savaragerow.query.metadata.GetForeignKeysQuery;
import nl.ssischaefer.savaragerow.query.metadata.GetPrimaryKeyColumnsQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableService {

    public void recreateTable(String table, List<SQLColumn> columns) throws Exception {
        String temporaryName = "table" + "_temp";
        List<String> currentColumns = new GetColumnsQuery().setTable(table).execute().stream().map(SQLColumn::getName).collect(Collectors.toList());
        List<String> selectColumns = currentColumns.stream().filter(c -> columns.stream().anyMatch(x -> x.getName().equals(c))).collect(Collectors.toList());

        new CreateTableQuery().setTableName(temporaryName).setColumns(columns).execute();
        new InsertRowQuery().setFromTable(table).setToTable(temporaryName).setSelectColumns(selectColumns).execute();
        new DeleteTableQuery().setTable(table).execute();
        new RenameTableQuery().setFrom(temporaryName).setTo(table).execute();
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
