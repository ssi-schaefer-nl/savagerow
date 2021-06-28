package nl.ssischaefer.savaragerow.workflow.action;

import nl.ssischaefer.savaragerow.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.workflow.WorkflowType;
import nl.ssischaefer.savaragerow.util.PlaceholderResolver;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateAction extends CrudAction {
    private String table;
    private List<RowCriterion> rowCriteria;
    private List<FieldUpdate> fieldUpdates;


    @Override
    public void perform(Map<String, String> data) {
        List<RowCriterion> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);
        List<FieldUpdate> transformedFieldUpdates = transformPlaceholdersFieldupdates(data, fieldUpdates);

        try {
            new UpdateRowQuery()
                    .setTable(table)
                    .setCriteria(transformedRowCriteria)
                    .setFieldUpdates(transformedFieldUpdates)
                    .execute();

            if(triggerWorkflows) {
                List<Map<String, String>> updatedRows = new GetRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute().getResult();
                updatedRows.forEach(r -> WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(r).setTable(table).setType(WorkflowType.UPDATE)));
            }


        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private List<FieldUpdate> transformPlaceholdersFieldupdates(Map<String, String> data, List<FieldUpdate> target) {
        List<FieldUpdate> temp = new ArrayList<>();
        for(FieldUpdate fieldUpdate : target) {
            String t = PlaceholderResolver.resolve(fieldUpdate.getValue(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new FieldUpdate().setColumn(fieldUpdate.getColumn()).setAction(fieldUpdate.getAction()).setValue(t));

        }
        return temp;
    }

    private List<RowCriterion> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriterion> target) {
        List<RowCriterion> temp = new ArrayList<>();
        for(RowCriterion criteria : target) {
            String t = PlaceholderResolver.resolve(criteria.getRequired(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new RowCriterion().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

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

    public List<RowCriterion> getRowCriteria() {
        return rowCriteria;
    }

    public UpdateAction setRowCriteria(List<RowCriterion> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }

}
