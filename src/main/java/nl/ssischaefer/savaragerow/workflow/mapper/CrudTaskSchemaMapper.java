package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.common.event.TableEventProducer;
import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.common.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.common.schema.CrudWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.data.operations.DynamicRepository;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.model.task.crud.*;

import java.util.List;
import java.util.stream.Collectors;

public class CrudTaskSchemaMapper {
    private final DynamicRepository dynamicRepository;
    private final TableEventProducer eventProducer;

    public CrudTaskSchemaMapper(DynamicRepository dynamicRepository, TableEventProducer eventProducer) {
        this.dynamicRepository = dynamicRepository;
        this.eventProducer = eventProducer;
    }

    public AbstractWorkflowTask mapSchemaToTask(CrudWorkflowTaskSchema taskSchema) {
        AbstractCrudWorkflowTask task = null;

        switch(taskSchema.getOperation()) {
            case "insert":
                task = new CrudInsertTask().setRowTemplate(taskSchema.getRowTemplate());
                break;
            case "update":
                task = new CrudUpdateTask()
                        .setUpdateInstructionTemplates(mapUpdateInstructions(taskSchema))
                        .setRowSelectionCriteriaTemplate(mapRowSelectionCriteria(taskSchema));
                break;
            case "delete":
                task = new CrudDeleteTask().setRowSelectionCriteriaTemplate(mapRowSelectionCriteria(taskSchema));
                break;
            case "select":
                task = new CrudSelectTask().setRowSelectionCriteriaTemplate(mapRowSelectionCriteria(taskSchema));
                break;
            default: return null;
        }
        task.setTable(taskSchema.getTable());
        task.setId(taskSchema.getId());
        task.setRepository(dynamicRepository);
        task.setEventProducer(eventProducer);
        return task;
    }

    private List<RowSelectionCriterion> mapRowSelectionCriteria(CrudWorkflowTaskSchema taskSchema) {
        return taskSchema.getRowSelectionCriteria().stream()
                .map(c -> new RowSelectionCriterion(c.getColumn(), c.getComparator(), c.getValue()))
                .collect(Collectors.toList());
    }

    private List<UpdateInstruction> mapUpdateInstructions(CrudWorkflowTaskSchema taskSchema) {
        return taskSchema.getUpdateInstructions().stream()
                .map(i -> new UpdateInstruction(i.getField(), i.getOperation(), i.getValue()))
                .collect(Collectors.toList());
    }

}
