package nl.ssischaefer.savaragerow.v2.service;

import nl.ssischaefer.savaragerow.v2.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v2.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.v2.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.v2.workflow.WorkflowType;
import nl.ssischaefer.savaragerow.v2.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v2.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.Map;

public class RowService {
    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).execute();

        WorkflowTask task = new WorkflowTask()
                .setData(row)
                .setTable(table)
                .setType(WorkflowType.UPDATE);

        WorkflowTaskQueue.getQueue().feed(task);
    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).execute().getGeneratedKey();
        Map<String, String> res = new GetRowQuery().setTable(table).setRowId(generatedId).execute().getResult().get(0);

        WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(res).setTable(table).setType(WorkflowType.INSERT));
        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data =  new GetRowQuery().setTable(table).setRowId(rowid).execute().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).execute();
        WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(data).setTable(table).setType(WorkflowType.DELETE));
    }
}
