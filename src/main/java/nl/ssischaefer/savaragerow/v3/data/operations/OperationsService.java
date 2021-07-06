package nl.ssischaefer.savaragerow.v3.data.operations;

import nl.ssischaefer.savaragerow.v3.data.operations.query.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v3.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.v3.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.v3.data.operations.query.UpdateRowQuery;

import java.util.Map;

public class OperationsService {
    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).executeUpdate();

    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).executeUpdate().getGeneratedKey();
        Map<String, String> res = new FindRowQuery().setTable(table).setRowId(generatedId).executeQuery().getResult().get(0);

        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data =  new FindRowQuery().setTable(table).setRowId(rowid).executeQuery().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).executeUpdate();
    }
}
