package nl.ssischaefer.savaragerow.v2.query;


import nl.ssischaefer.savaragerow.v2.model.RowCriteria;
import nl.ssischaefer.savaragerow.v2.util.OperatorTransformer;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DeleteRowByCriteriaQuery {
    private String table;
    private List<RowCriteria> criteria;
    private List<Map<String, String>> deletedRows = new ArrayList<>();
    private boolean storeDeletedRows = false;

    private PreparedStatement preparedStatement;

    public DeleteRowByCriteriaQuery setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteRowByCriteriaQuery setCriteria(List<RowCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    public DeleteRowByCriteriaQuery generate() throws SQLException {
        String whereClause = criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), OperatorTransformer.convertToSql(c.getOperator())))
                .collect(Collectors.joining(" AND "));

        if(storeDeletedRows)
            deletedRows = new GetRowQuery().setTable(table).setCriteria(criteria).generate().execute().getResult();

        String sql = String.format("DELETE FROM %s WHERE %s", this.table, whereClause);
        this.preparedStatement = SQLiteDataSource.get().prepareStatement(sql);
        int nextParamIndex = 0;

        for (RowCriteria c : criteria) {
            nextParamIndex++;
            preparedStatement.setString(nextParamIndex, c.getRequired());
        }

        return this;

    }

    public DeleteRowByCriteriaQuery execute() throws SQLException {
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return this;
    }

    public List<Map<String, String>> getDeletedRows() {
        return deletedRows;
    }

    public boolean isStoreDeletedRows() {
        return storeDeletedRows;
    }

    public DeleteRowByCriteriaQuery setStoreDeletedRows(boolean storeDeletedRows) {
        this.storeDeletedRows = storeDeletedRows;
        return this;
    }
}
