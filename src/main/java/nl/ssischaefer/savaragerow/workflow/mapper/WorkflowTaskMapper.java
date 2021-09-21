package nl.ssischaefer.savaragerow.workflow.mapper;


import nl.ssischaefer.savaragerow.shared.schema.AbstractWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.shared.schema.CrudWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.shared.schema.DecisionWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorkflowTaskMapper {
    private final CrudTaskSchemaMapper crudTaskSchemaMapper;
    private final DecisionTaskSchemaMapper decisionTaskSchemaMapper;

    public WorkflowTaskMapper(CrudTaskSchemaMapper crudTaskSchemaMapper, DecisionTaskSchemaMapper decisionTaskSchemaMapper) {
        this.crudTaskSchemaMapper = crudTaskSchemaMapper;
        this.decisionTaskSchemaMapper = decisionTaskSchemaMapper;
    }

    public List<AbstractWorkflowTask> createTasks(List<AbstractWorkflowTaskSchema> tasksSchema) {
        return tasksSchema.stream().map(this::createTask).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public AbstractWorkflowTask createTask(AbstractWorkflowTaskSchema taskSchema) {
        String type = taskSchema.getTaskType();
        AbstractWorkflowTask task = null;

        if (type.equalsIgnoreCase("crud") && taskSchema instanceof CrudWorkflowTaskSchema) {
            task = crudTaskSchemaMapper.mapSchemaToTask((CrudWorkflowTaskSchema) taskSchema);
        } else if (type.equalsIgnoreCase("decision") && taskSchema instanceof DecisionWorkflowTaskSchema) {
            task = decisionTaskSchemaMapper.mapSchemaToTask((DecisionWorkflowTaskSchema) taskSchema);
        }

        if (task != null)
            task.setPropagatedParameters(taskSchema.getPropagatedParameters());
        return task;
    }

}

