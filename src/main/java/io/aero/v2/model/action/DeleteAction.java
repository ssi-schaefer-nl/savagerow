package io.aero.v2.model.action;

import io.aero.v2.query.DeleteRowByCriteriaQuery;
import io.aero.v2.util.StringPlaceholderTransformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private String table;
    private List<RowCriteria> rowCriteria;

    @Override
    public void perform(Map<String, String> oldData) {
        List<RowCriteria> transformedRowCriteria = transformPlaceholders(oldData, rowCriteria);

        try {
            new DeleteRowByCriteriaQuery().setTable(table).setCriteria(transformedRowCriteria).generate().execute();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<RowCriteria> transformPlaceholders(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for(RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transform(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setOperator(criteria.getOperator()).setRequired(t));

        }
        return temp;
    }


    public String getTable() {
        return table;
    }

    public DeleteAction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public DeleteAction setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }
}
