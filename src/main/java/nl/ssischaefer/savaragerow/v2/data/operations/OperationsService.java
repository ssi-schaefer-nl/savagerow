package nl.ssischaefer.savaragerow.v2.data.operations;

import nl.ssischaefer.savaragerow.v1.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v1.workflow.triggeredworkflow.OperationType;
import nl.ssischaefer.savaragerow.v2.workflow.WorkflowType;
import nl.ssischaefer.savaragerow.v2.workflow.queue.EventProducer;

import java.util.List;
import java.util.Map;

public class OperationsService {
    private EventProducer triggerProducer;
    private WorkflowService workflowService;

    public OperationsService(EventProducer triggerProducer, WorkflowService workflowService) {
        this.triggerProducer = triggerProducer;
        this.workflowService = workflowService;
    }

    public void update(String table, Map<String, String> row, Long rowid) throws Exception {
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(rowid).execute();

        WorkflowService.WorkflowSearchCriteria criteria = new WorkflowService.WorkflowSearchCriteria().setTable(table).setWorkflowType(WorkflowType.TRIGGERED).setOperationType(OperationType.UPDATE);
        List<String> workflows = workflowService.find()

        // Find workflows for type = update on table = table
        // create triggers for workflows
    }

    public Map<String, String> insert(String table, Map<String, String>  row) throws Exception {
        Long generatedId = new InsertRowQuery().setToTable(table).setRow(row).execute().getGeneratedKey();
        Map<String, String> res = new GetRowQuery().setTable(table).setRowId(generatedId).execute().getResult().get(0);

//        workflowService.feedToQueue(res, new WorkflowService.WorkflowSearchCriteria().setTable(table).setType(WorkflowType.INSERT));
        return res;
    }

    public void delete(String table, Long rowid) throws Exception {
        Map<String, String> data = new GetRowQuery().setTable(table).setRowId(rowid).execute().getResult().get(0);
        new DeleteRowQuery().setTable(table).setRow(rowid).execute();

//        workflowService.feedToQueue(data, new WorkflowService.WorkflowSearchCriteria().setTable(table).setType(WorkflowType.DELETE));
    }

}
