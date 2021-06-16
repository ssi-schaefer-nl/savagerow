package io.aero.v2.model.action;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.query.InsertRowQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InsertAction extends CrudAction {
    private String table;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> oldData) {
        Map<String, String> transformedRow = transformPlaceholders(oldData, row);
        try {
            new InsertRowQuery().setTable(table).setData(new RowDTO().setRow(transformedRow)).generate().execute();
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

    public Map<String, String> getRow() {
        return row;
    }

    public InsertAction setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

    public String getTable() {
        return table;
    }

    public InsertAction setTable(String table) {
        this.table = table;
        return this;
    }
}
