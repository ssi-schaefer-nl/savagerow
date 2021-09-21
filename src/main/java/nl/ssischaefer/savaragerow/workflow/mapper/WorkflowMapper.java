package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.Workflow;

public class WorkflowMapper {
    private final WorkflowTaskMapper taskMapper;
    private final WorkflowTriggerMapper triggerMapper;

    public WorkflowMapper(WorkflowTaskMapper taskMapper, WorkflowTriggerMapper triggerMapper) {
        this.taskMapper = taskMapper;
        this.triggerMapper = triggerMapper;
    }

    public Workflow createWorkflow(WorkflowSchema schema) {
        var tasks = taskMapper.createTasks(schema.getTasks());
        var trigger = triggerMapper.createTrigger(schema);

        Workflow workflow = new Workflow();
        workflow.setWorkflowId(schema.getId());
        workflow.setTasks(tasks);
        workflow.setTrigger(trigger);
        return workflow;
    }
}
