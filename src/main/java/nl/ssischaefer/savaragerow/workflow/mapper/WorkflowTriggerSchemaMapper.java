package nl.ssischaefer.savaragerow.workflow.mapper;


import nl.ssischaefer.savaragerow.common.schema.WorkflowTriggerSchema;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.AbstractWorkflowTrigger;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableTrigger;

public class WorkflowTriggerSchemaMapper {
    public AbstractWorkflowTrigger mapSchemaToModel(WorkflowTriggerSchema triggerSchema) {
        switch (triggerSchema.getTriggerType()) {
            case "table":
                return mapSchemaToTableTrigger(triggerSchema);
            default:
                return null;
        }
    }

    private AbstractWorkflowTrigger mapSchemaToTableTrigger(WorkflowTriggerSchema triggerSchema) {
        var t = new TableTrigger();
        t.setEvent(triggerSchema.getTableEvent());
        t.setTable(triggerSchema.getTable());
        return t;
    }
}
