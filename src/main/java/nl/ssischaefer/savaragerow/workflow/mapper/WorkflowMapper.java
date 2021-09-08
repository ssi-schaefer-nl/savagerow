package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.common.schema.AbstractWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.common.schema.WorkflowTriggerSchema;
import nl.ssischaefer.savaragerow.workflow.exceptions.NoInitialTaskException;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.AbstractWorkflowTrigger;
import nl.ssischaefer.savaragerow.workflow.util.TaskLinker;

import java.util.List;
import java.util.Objects;

public class WorkflowMapper {
    private final WorkflowTaskSchemaMapper taskSchemaMapper;
    private final WorkflowTriggerSchemaMapper triggerSchemaMapper;

    public WorkflowMapper(WorkflowTaskSchemaMapper taskSchemaMapper, WorkflowTriggerSchemaMapper triggerSchemaMapper) {
        this.taskSchemaMapper = taskSchemaMapper;
        this.triggerSchemaMapper = triggerSchemaMapper;
    }

    public List<AbstractWorkflowTask> mapSchemaToTasks(List<AbstractWorkflowTaskSchema> taskSchemas) {
        var tasks = taskSchemaMapper.mapSchemaToModel(taskSchemas);
        return TaskLinker.linkTasks(tasks, taskSchemas);
    }

    public AbstractWorkflowTrigger mapSchemaToTrigger(WorkflowTriggerSchema triggerSchema, List<AbstractWorkflowTask> tasks)  {
        var trigger = triggerSchemaMapper.mapSchemaToModel(triggerSchema);
        tasks.stream()
                .filter(x -> x.getId().equals(triggerSchema.getTask()))
                .findFirst()
                .ifPresent(trigger::setInitialTask);

        return trigger;
    }
}
