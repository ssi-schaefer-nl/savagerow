package nl.ssischaefer.savaragerow.v2.savaragerow.workflows;

import nl.ssischaefer.savaragerow.v2.data.operations.query.dql.FindRowQuery;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.PlaceholderResolver;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TableCondition {
    private String table;
    private boolean match;
    private List<RowCriterion> rowCriteria;

    public String getTable() {
        return table;
    }

    public TableCondition setTable(String table) {
        this.table = table;
        return this;
    }

    public List<RowCriterion> getRowCriteria() {
        return rowCriteria;
    }

    public TableCondition setRowCriteria(List<RowCriterion> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }

    public boolean isSatisfied(Map<String, String> data) throws SQLException {
        List<RowCriterion> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);

        return new FindRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute().getResult().isEmpty() != match;
    }

    private List<RowCriterion> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriterion> target) {
        List<RowCriterion> temp = new ArrayList<>();
        for (RowCriterion criteria : target) {
            String t = PlaceholderResolver.resolve(criteria.getRequired(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new RowCriterion().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

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
