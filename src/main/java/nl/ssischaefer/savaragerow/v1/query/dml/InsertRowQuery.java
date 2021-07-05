package nl.ssischaefer.savaragerow.v1.query.dml;


import nl.ssischaefer.savaragerow.v1.query.AbstractUpdateQuery;
import nl.ssischaefer.savaragerow.v1.util.sql.PreparedStatementParameterHelper;
import nl.ssischaefer.savaragerow.v1.util.sql.SQLDMLGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InsertRowQuery extends AbstractUpdateQuery {
    private String toTable;
    private String fromTable;
    private List<String> selectColumns;
    private Map<String, String> row;



    @Override
    protected PreparedStatement generate(Connection sqlConnection) throws Exception {
        if(row != null) {
            List<String> columns = row.keySet().stream().filter(c -> !c.equals("rowid")).filter(c -> !row.get(c).isEmpty()).collect(Collectors.toList());
            if(columns.isEmpty()) throw new Exception("The row must have at least one field with data to insert a row");

            String sql = SQLDMLGenerator.generateInsertIntoQuery(toTable, columns);
            PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
            PreparedStatementParameterHelper.setForRow(preparedStatement, row, columns);
            return preparedStatement;
        }
        else {
            String sql = SQLDMLGenerator.generateInsertIntoQuery(toTable, fromTable, selectColumns);
            return sqlConnection.prepareStatement(sql);
        }
    }

    public InsertRowQuery setToTable(String toTable) {
        this.toTable = toTable;
        return this;
    }

    public InsertRowQuery setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public InsertRowQuery setFromTable(String fromTable) {
        this.fromTable = fromTable;
        return this;
    }

    public InsertRowQuery setSelectColumns(List<String> selectColumns) {
        this.selectColumns = selectColumns;
        return this;
    }
}
