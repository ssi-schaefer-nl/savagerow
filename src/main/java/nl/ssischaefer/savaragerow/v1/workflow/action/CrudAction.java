package nl.ssischaefer.savaragerow.v1.workflow.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowCache;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v1.workflow.WorkflowService.WorkflowSearchCriteria;
import nl.ssischaefer.savaragerow.v1.workflow.triggeredworkflow.OperationType;
import nl.ssischaefer.savaragerow.v1.workflow.workflowqueue.WorkflowQueue;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "crudType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InsertAction.class, name = "insert"),
})
public abstract class CrudAction extends Action {
    protected String table;
    protected boolean triggerWorkflows;
    private WorkflowService workflowService;

    public CrudAction() {
        this.workflowService = new WorkflowService(WorkflowCache.get(), WorkflowQueue.getQueue());
    }


    public void perform(Map<String, String> data) {
        List<Map< String, String>> result = null;
        try {
            result = execute(data);
            if(triggerWorkflows)
                triggerWorkflows(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void triggerWorkflows(List<Map<String, String>> result) {
        if(result == null) return;

        result.forEach(d -> {
            try {
                WorkflowSearchCriteria criteria = new WorkflowSearchCriteria().setTable(table).setOperationType(OperationType.fromString(getType()));
                workflowService.feedToQueue(d, criteria);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected abstract List<Map<String, String>> execute(Map<String, String> data) throws Exception;

    public boolean isTriggerWorkflows() {
        return triggerWorkflows;
    }

    public CrudAction setTriggerWorkflows(boolean triggerWorkflows) {
        this.triggerWorkflows = triggerWorkflows;
        return this;
    }

    public String getTable() {
        return table;
    }

    public CrudAction setTable(String table) {
        this.table = table;
        return this;
    }

    public CrudAction setWorkflowService(WorkflowService workflowService) {
        this.workflowService = workflowService;
        return this;
    }
}
