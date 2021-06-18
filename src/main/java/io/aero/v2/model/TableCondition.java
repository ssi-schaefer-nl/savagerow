package io.aero.v2.model;

import io.aero.v2.query.GetRowQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableCondition {
    private String table;
    private boolean match;
    private List<RowCriteria> rowCriteria;

    public String getTable() {
        return table;
    }

    public TableCondition setTable(String table) {
        this.table = table;
        return this;
    }

    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public TableCondition setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }

    public boolean isSatisfied(Map<String, String> data) throws SQLException {
        List<RowCriteria> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);

        return new GetRowQuery().setTable(table).setCriteria(transformedRowCriteria).generate().execute().getResult().isEmpty() != match;
    }

    private List<RowCriteria> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for (RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setOperator(criteria.getOperator()).setRequired(t));

        }
        return temp;
    }

    public boolean isMatch() {
        return match;
    }

    public TableCondition setMatch(boolean match) {
        this.match = match;
        return this;
    }
}
