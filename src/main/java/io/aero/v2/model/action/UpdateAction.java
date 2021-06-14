package io.aero.v2.model.action;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.query.UpdateRowByCriteriaQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAction extends CrudAction {
    private String table;
    private List<RowCriteria> rowCriteria;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> data) {
        List<RowCriteria> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);
        Map<String, String> transformedRow = transformPlaceholders(data, row);

        try {
            new UpdateRowByCriteriaQuery().setTable(table).setRow(new RowDTO().setRow(transformedRow)).setCriteria(transformedRowCriteria).generate().execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private Map<String, String> transformPlaceholders(Map<String, String> data, Map<String, String> target) {
        Map<String, String> temp = new HashMap<>();
        target.forEach((key, value) -> {
            String t = StringPlaceholderTransformer.transform(value, data);
            temp.put(key, t);
        });
        return temp;
    }

    private List<RowCriteria> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for(RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transform(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setOperator(criteria.getOperator()).setRequired(t));

        }
        return temp;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public UpdateAction setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public String getTable() {
        return table;
    }

    public UpdateAction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public UpdateAction setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }
}
