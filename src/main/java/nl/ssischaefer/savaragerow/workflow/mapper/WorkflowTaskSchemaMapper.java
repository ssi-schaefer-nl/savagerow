package nl.ssischaefer.savaragerow.workflow.mapper;


import nl.ssischaefer.savaragerow.common.event.TableEventProducer;
import nl.ssischaefer.savaragerow.common.schema.AbstractWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.common.schema.CrudWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class WorkflowTaskSchemaMapper {
    private final CrudTaskSchemaMapper crudTaskSchemaMapper;

    public WorkflowTaskSchemaMapper(CrudTaskSchemaMapper crudTaskSchemaMapper) {
        this.crudTaskSchemaMapper = crudTaskSchemaMapper;
    }

    public List<AbstractWorkflowTask> mapSchemaToModel(List<AbstractWorkflowTaskSchema> tasksSchema) {
        return tasksSchema.stream().map(this::mapSchemaToModel).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public AbstractWorkflowTask mapSchemaToModel(AbstractWorkflowTaskSchema taskSchema) {
        String type = taskSchema.getTaskType();
        if (type.equalsIgnoreCase("crud") && taskSchema instanceof CrudWorkflowTaskSchema) {
            return crudTaskSchemaMapper.mapSchemaToTask((CrudWorkflowTaskSchema) taskSchema);
        }
        return null;
    }

}

