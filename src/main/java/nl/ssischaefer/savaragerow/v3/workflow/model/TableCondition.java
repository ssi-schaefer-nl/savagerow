package nl.ssischaefer.savaragerow.v3.workflow.model;

import nl.ssischaefer.savaragerow.v3.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.v3.util.StringPlaceholderTransformer;

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

    public boolean isSatisfied(Map<String, String> data) throws Exception {
        List<RowCriteria> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);

        return new FindRowQuery().setTable(table).setCriteria(transformedRowCriteria).executeQuery().getResult().isEmpty() != match;
    }

    private List<RowCriteria> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for (RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

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
