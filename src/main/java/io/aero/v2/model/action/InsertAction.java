package io.aero.v2.model.action;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.query.InsertRowQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.Map;

public class InsertAction extends CrudAction {
    private String table;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> data) {
        transformPlaceholders(data);
        try {
            new InsertRowQuery().setTable(table).setData(new RowDTO().setRow(row)).generate().execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void transformPlaceholders(Map<String, String> data) {
        for(Map.Entry<String, String> entry : row.entrySet()) {
            String t = StringPlaceholderTransformer.transform(entry.getValue(), data);
            entry.setValue(t);
        }
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
