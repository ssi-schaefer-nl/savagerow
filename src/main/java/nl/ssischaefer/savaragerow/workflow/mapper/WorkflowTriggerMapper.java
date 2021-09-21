package nl.ssischaefer.savaragerow.workflow.mapper;


import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.shared.schema.WorkflowTriggerSchema;
import nl.ssischaefer.savaragerow.workflow.StorageAdapter;
import nl.ssischaefer.savaragerow.workflow.trigger.TableEventTrigger;
import nl.ssischaefer.savaragerow.workflow.trigger.AbstractTrigger;
import nl.ssischaefer.savaragerow.workflow.trigger.ScheduledTrigger;

public class WorkflowTriggerMapper {
    private final StorageAdapter storageAdapter;

    public WorkflowTriggerMapper(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    public AbstractTrigger createTrigger(WorkflowSchema workflowSchema) {
        var triggerSchema = workflowSchema.getTrigger();
        AbstractTrigger workflowTrigger = null;

        switch (triggerSchema.getTriggerType()) {
            case "table":
                workflowTrigger = createTableTrigger(triggerSchema);
                break;
            case "scheduled":
                workflowTrigger = createScheduledTrigger(triggerSchema);
                break;
            default:
                break;
        }

        if(workflowTrigger != null) {
            workflowTrigger.setWorkflow(workflowSchema.getId());
            workflowTrigger.setTask(triggerSchema.getTask());
        }
        return workflowTrigger;
    }

    private AbstractTrigger createScheduledTrigger(WorkflowTriggerSchema triggerSchema) {
        return new ScheduledTrigger();
    }

    private AbstractTrigger createTableTrigger(WorkflowTriggerSchema triggerSchema) {
        var t = new TableEventTrigger(storageAdapter);
        t.setEvent(triggerSchema.getTableEvent());
        t.setTable(triggerSchema.getTable());
        return t;
    }
}