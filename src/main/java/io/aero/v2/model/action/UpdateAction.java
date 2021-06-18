package io.aero.v2.model.action;

import io.aero.v2.model.FieldUpdate;
import io.aero.v2.model.RowCriteria;
import io.aero.v2.model.WorkflowType;
import io.aero.v2.query.UpdateRowsByCriteriaAndTransformActions;
import io.aero.v2.util.StringPlaceholderTransformer;
import io.aero.v2.workflowqueue.WorkflowTask;
import io.aero.v2.workflowqueue.WorkflowTaskQueue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateAction extends CrudAction {
    private String table;
    private List<RowCriteria> rowCriteria;
    private List<FieldUpdate> fieldUpdates;


    @Override
    public void perform(Map<String, String> data) {
        List<RowCriteria> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);
        List<FieldUpdate> transformedFieldUpdates = transformPlaceholdersFieldupdates(data, fieldUpdates);

        try {
            List<Map<String, String>> updatedRows = new UpdateRowsByCriteriaAndTransformActions()
                    .setTable(table)
                    .setCriteria(transformedRowCriteria)
                    .setFieldUpdates(transformedFieldUpdates)
                    .setStoreUpdatedRows(triggerWorkflows)
                    .generate()
                    .execute()
                    .getUpdatedRows();
            System.out.println("find me");
            System.out.println(updatedRows);
            if(triggerWorkflows) {
                updatedRows.forEach(r -> WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(r).setTable(table).setType(WorkflowType.UPDATE)));
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<FieldUpdate> transformPlaceholdersFieldupdates(Map<String, String> data, List<FieldUpdate> target) {
        List<FieldUpdate> temp = new ArrayList<>();
        for(FieldUpdate fieldUpdate : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(fieldUpdate.getValue(), data);
            temp.add(new FieldUpdate().setColumn(fieldUpdate.getColumn()).setAction(fieldUpdate.getAction()).setValue(t));

        }
        return temp;
    }

    private List<RowCriteria> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for(RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setOperator(criteria.getOperator()).setRequired(t));

        }
        return temp;
    }

    public List<FieldUpdate> getFieldUpdates() {
        return fieldUpdates;
    }

    public UpdateAction setFieldUpdates(List<FieldUpdate> fieldUpdates) {
        this.fieldUpdates = fieldUpdates;
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
