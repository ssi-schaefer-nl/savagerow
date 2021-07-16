package nl.ssischaefer.savaragerow.data.operations;

import nl.ssischaefer.savaragerow.data.operations.query.DeleteRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.UpdateRowQuery;
import nl.ssischaefer.savaragerow.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskProducer;

import java.util.List;
import java.util.Map;

public class OperationsService {
    private final WorkflowTaskProducer taskProducer;

    public OperationsService(WorkflowTaskProducer taskProducer) {
        this.taskProducer = taskProducer;
    }

    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).executeUpdate();

        WorkflowTask workflowTask = new WorkflowTask().setTable(table).setData(row).setType(WorkflowTriggerType.UPDATE);
        taskProducer.produce(workflowTask);
    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).executeUpdate().getGeneratedKey();
        Map<String, String> res = new FindRowQuery().setTable(table).setRowId(generatedId).executeQuery().getResult().get(0);

        WorkflowTask workflowTask = new WorkflowTask().setTable(table).setData(res).setType(WorkflowTriggerType.INSERT);
        taskProducer.produce(workflowTask);
        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data =  new FindRowQuery().setTable(table).setRowId(rowid).executeQuery().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).executeUpdate();

        WorkflowTask task = new WorkflowTask().setTable(table).setData(data).setType(WorkflowTriggerType.DELETE);
        taskProducer.produce(task);
    }

    public List<Map<String, String>> getAll(String table) throws Exception {
        return new FindRowQuery().setTable(table).executeQuery().getResult();
    }

    public List<Map<String, String>> getByRowId(String table, Long rowId) throws Exception {
        return new FindRowQuery().setTable(table).setRowId(rowId).executeQuery().getResult();
    }
}
