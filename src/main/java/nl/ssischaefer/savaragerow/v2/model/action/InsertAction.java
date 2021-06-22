package nl.ssischaefer.savaragerow.v2.model.action;

import nl.ssischaefer.savaragerow.v2.dto.RowDTO;
import nl.ssischaefer.savaragerow.v2.model.WorkflowType;
import nl.ssischaefer.savaragerow.v2.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.util.StringPlaceholderTransformer;
import nl.ssischaefer.savaragerow.v2.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v2.workflowqueue.WorkflowTaskQueue;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InsertAction extends CrudAction {
    private String table;
    private Map<String, String> row;

    @Override
    public void perform(Map<String, String> data) {
        Map<String, String> transformedRow = transformPlaceholders(data, row);
        try {
            new InsertRowQuery().setTable(table).setData(new RowDTO().setRow(transformedRow)).generate().execute();
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
