package nl.ssischaefer.savaragerow.workflow.action;

import nl.ssischaefer.savaragerow.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;
import nl.ssischaefer.savaragerow.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.util.PlaceholderResolver;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private String table;
    private List<RowCriterion> rowCriteria;

    @Override
    public void perform(Map<String, String> data) {
        List<RowCriterion> transformedRowCriteria = transformPlaceholders(data, rowCriteria);

        try {
            List<Map<String, String>> deletedRows = new GetRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute().getResult();
            new DeleteRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute();

            if (triggerWorkflows) {
                deletedRows.forEach(r -> WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(r).setTable(table).setType(WorkflowType.DELETE)));
            }

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private List<RowCriterion> transformPlaceholders(Map<String, String> data, List<RowCriterion> target) {
        List<RowCriterion> temp = new ArrayList<>();
        for (RowCriterion criteria : target) {
            String t = PlaceholderResolver.resolve(criteria.getRequired(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new RowCriterion().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

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

    public List<RowCriterion> getRowCriteria() {
        return rowCriteria;
    }

    public DeleteAction setRowCriteria(List<RowCriterion> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }


}
