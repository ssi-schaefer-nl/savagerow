package nl.ssischaefer.savaragerow.v3.workflow.model.action;

import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowType;
import nl.ssischaefer.savaragerow.v3.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.v3.util.StringPlaceholderTransformer;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.HashMap;
import java.util.Map;

public class InsertAction extends CrudAction {
    private String table;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> data) {
        Map<String, String> transformedRow = transformPlaceholders(data, row);
        try {
            new InsertRowQuery().setToTable(table).setRow(transformedRow).executeUpdate();
            if(triggerWorkflows) WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(transformedRow).setTable(table).setType(WorkflowType.INSERT));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }


    private Map<String, String> transformPlaceholders(Map<String, String> data, Map<String, String> target) {
        Map<String, String> temp = new HashMap<>();
        target.forEach((key, value) -> {
            String t = StringPlaceholderTransformer.transformPlaceholders(value, data);
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
