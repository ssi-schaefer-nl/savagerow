package io.aero.v2.model.action;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.query.DeleteRowByCriteriaQuery;
import io.aero.v2.query.UpdateRowByCriteriaQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private String table;
    private Map<String, String> rowCriteria;

    @Override
    public void perform(Map<String, String> data) {
        Map<String, String> transformedRowCriteria = transformPlaceholders(data, rowCriteria);

        try {
            new DeleteRowByCriteriaQuery().setTable(table).setCriteria(transformedRowCriteria).generate().execute();
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


    public String getTable() {
        return table;
    }

    public DeleteAction setTable(String table) {
        this.table = table;
        return this;
    }

    public DeleteAction setRowCriteria(Map<String, String> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }

    public Map<String, String> getRowCriteria() {
        return rowCriteria;
    }
}
