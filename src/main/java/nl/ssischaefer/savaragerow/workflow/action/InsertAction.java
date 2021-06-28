package nl.ssischaefer.savaragerow.workflow.action;

import nl.ssischaefer.savaragerow.workflow.WorkflowType;
import nl.ssischaefer.savaragerow.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.util.PlaceholderResolver;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.HashMap;
import java.util.Map;

public class InsertAction extends CrudAction {
    private String table;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> data) {
        Map<String, String> transformedRow = transformPlaceholders(data, row);
        try {
            new InsertRowQuery().setToTable(table).setRow(transformedRow).execute();
            if(triggerWorkflows) WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(transformedRow).setTable(table).setType(WorkflowType.INSERT));
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
    }


    private Map<String, String> transformPlaceholders(Map<String, String> data, Map<String, String> target) {
        Map<String, String> temp = new HashMap<>();
        target.forEach((key, value) -> {
            String t = PlaceholderResolver.resolve(value, data, PlaceholderResolver.BRACKETS_FORMAT);
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
