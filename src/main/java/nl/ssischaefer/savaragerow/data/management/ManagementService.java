package nl.ssischaefer.savaragerow.data.management;

import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;
import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDataSource;
import nl.ssischaefer.savaragerow.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.data.management.query.CreateTableQuery;
import nl.ssischaefer.savaragerow.data.management.query.DeleteTableQuery;
import nl.ssischaefer.savaragerow.data.management.query.RenameTableQuery;
import nl.ssischaefer.savaragerow.data.management.query.GetColumnsQuery;
import nl.ssischaefer.savaragerow.data.management.query.GetForeignKeysQuery;
import nl.ssischaefer.savaragerow.data.management.query.GetPrimaryKeyColumnsQuery;
import nl.ssischaefer.savaragerow.workspace.WorkspaceService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManagementService {

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
}
