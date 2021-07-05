package nl.ssischaefer.savaragerow.v2.savaragerow.query;

import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dql.FindRowQuery;
import nl.ssischaefer.savaragerow.v2.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.triggeredworkflow.WorkflowType;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService.WorkflowSearchCriteria;

import java.util.Map;

public class RowService {
    private final WorkflowService workflowService;

    public RowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).executeUpdate();

        workflowService.feedToQueue(row, new WorkflowSearchCriteria().setTable(table).setOperationType(WorkflowType.UPDATE));
    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).executeUpdate().getGeneratedKey();
        Map<String, String> res = new FindRowQuery().setTable(table).setRowId(generatedId).execute().getResult().get(0);

        workflowService.feedToQueue(res, new WorkflowSearchCriteria().setTable(table).setOperationType(WorkflowType.INSERT));
        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data = new FindRowQuery().setTable(table).setRowId(rowid).execute().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).executeUpdate();

        workflowService.feedToQueue(data, new WorkflowSearchCriteria().setTable(table).setOperationType(WorkflowType.DELETE));
    }
}
