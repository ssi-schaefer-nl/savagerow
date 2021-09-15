package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.common.schema.AbstractWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.AbstractWorkflowTrigger;
import nl.ssischaefer.savaragerow.workflow.util.TaskLinker;

import java.util.List;

public class WorkflowMapper {
    private final WorkflowTaskSchemaMapper taskSchemaMapper;
    private final WorkflowTriggerSchemaMapper triggerSchemaMapper;

    public WorkflowMapper(WorkflowTaskSchemaMapper taskSchemaMapper, WorkflowTriggerSchemaMapper triggerSchemaMapper) {
        this.taskSchemaMapper = taskSchemaMapper;
        this.triggerSchemaMapper = triggerSchemaMapper;
    }

    public AbstractWorkflowTask mapSchemaToTask(AbstractWorkflowTaskSchema taskSchema) {
        return taskSchemaMapper.mapSchemaToModel(taskSchema);
    }

    public List<AbstractWorkflowTask> mapWorkflowSchemaToTasks(WorkflowSchema wf) {
        var taskSchemas = wf.getTasks();
        var tasks = taskSchemaMapper.mapSchemaToModel(taskSchemas);
        tasks.forEach(t -> t.setWorkflowId(wf.getId()));
        return TaskLinker.linkTasks(tasks, taskSchemas);
    }

    public AbstractWorkflowTrigger mapWorkflowSchemaToTrigger(WorkflowSchema wf, List<AbstractWorkflowTask> tasks)  {
        var triggerSchema = wf.getTrigger();
        var trigger = triggerSchemaMapper.mapSchemaToModel(triggerSchema);
        tasks.stream()
                .filter(x -> x.getId().equals(triggerSchema.getTask()))
                .findFirst()
                .ifPresent(trigger::setInitialTask);

        trigger.setWorkflowId(wf.getId());
        return trigger;
    }
}
