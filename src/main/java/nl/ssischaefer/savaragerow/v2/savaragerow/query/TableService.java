package nl.ssischaefer.savaragerow.v2.savaragerow.query;

import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.RenameTableQuery;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.metadata.GetColumnsQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.metadata.GetForeignKeysQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.metadata.GetPrimaryKeyColumnsQuery;
import nl.ssischaefer.savaragerow.v2.api.dto.SQLColumn;
import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.CreateTableQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.DeleteTableQuery;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableService {

    public void recreateTable(String table, List<SQLColumn> columns) throws Exception {
        String temporaryName = "table" + "_temp";
        List<String> currentColumns = new GetColumnsQuery().setTable(table).execute().stream().map(SQLColumn::getName).collect(Collectors.toList());
        List<String> selectColumns = currentColumns.stream().filter(c -> columns.stream().anyMatch(x -> x.getName().equals(c))).collect(Collectors.toList());

        new CreateTableQuery().setTableName(temporaryName).setColumns(columns).executeUpdate();
        new InsertRowQuery().setFromTable(table).setToTable(temporaryName).setSelectColumns(selectColumns).executeUpdate();
        new DeleteTableQuery().setTable(table).executeUpdate();
        new RenameTableQuery().setFrom(temporaryName).setTo(table).executeQuery();
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
