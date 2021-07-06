package nl.ssischaefer.savaragerow.v3.workflow.model.action;

import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowType;
import nl.ssischaefer.savaragerow.v3.data.operations.query.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v3.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.v3.util.StringPlaceholderTransformer;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private String table;
    private List<RowCriteria> rowCriteria;

    @Override
    public void perform(Map<String, String> data) {
        List<RowCriteria> transformedRowCriteria = transformPlaceholders(data, rowCriteria);

        try {
            List<Map<String, String>> deletedRows = new FindRowQuery().setTable(table).setCriteria(transformedRowCriteria).executeQuery().getResult();
            new DeleteRowQuery().setTable(table).setCriteria(transformedRowCriteria).executeUpdate();

            if (triggerWorkflows) {
                deletedRows.forEach(r -> WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(r).setTable(table).setType(WorkflowType.DELETE)));
            }

        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }

    private List<RowCriteria> transformPlaceholders(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for (RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
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
