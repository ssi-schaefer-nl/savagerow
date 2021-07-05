package nl.ssischaefer.savaragerow.v1.query;

import nl.ssischaefer.savaragerow.v1.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService.WorkflowSearchCriteria;
import nl.ssischaefer.savaragerow.v1.workflow.triggeredworkflow.OperationType;

import java.util.Map;

public class RowService {
    private final WorkflowService workflowService;

    public RowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }


    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).execute();

        workflowService.feedToQueue(row, new WorkflowSearchCriteria().setTable(table).setOperationType(OperationType.UPDATE));
    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).execute().getGeneratedKey();
        Map<String, String> res = new GetRowQuery().setTable(table).setRowId(generatedId).execute().getResult().get(0);

        workflowService.feedToQueue(res, new WorkflowSearchCriteria().setTable(table).setOperationType(OperationType.INSERT));
        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data = new GetRowQuery().setTable(table).setRowId(rowid).execute().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).execute();

        workflowService.feedToQueue(data, new WorkflowSearchCriteria().setTable(table).setOperationType(OperationType.DELETE));
    }
}
