package nl.ssischaefer.savaragerow.v3.workflow.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import nl.ssischaefer.savaragerow.v3.workflow.exceptions.WorkflowTaskQueueException;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTaskProducer;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTaskQueue;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "crudType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InsertAction.class, name = "insert"),
})
public abstract class CrudAction extends Action {
    protected boolean triggerWorkflows;
    protected String table;

    @Override
    public void perform(Map<String, String> data) {
        try {
            List<Map<String, String>> actionResult = performAction(data);
            if(triggerWorkflows) {
                WorkflowTaskProducer taskProducer = new WorkflowTaskProducer(WorkflowTaskQueue.getQueue());
                actionResult.forEach(r -> {
                    WorkflowTask task = new WorkflowTask().setTable(table).setData(r).setType(getTriggerType());
                    taskProducer.produce(task);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract List<Map<String, String>> performAction(Map<String, String> data) throws Exception;

    protected abstract WorkflowTriggerType getTriggerType();

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

}
