package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.shared.schema.CrudWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.workflow.StorageAdapter;
import nl.ssischaefer.savaragerow.workflow.TableEventProducer;
import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.task.crud.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrudTaskSchemaMapper {
    private final StorageAdapter storageAdapter;
    private final TableEventProducer eventProducer;

    public CrudTaskSchemaMapper(StorageAdapter storageAdapter, TableEventProducer eventProducer) {
        this.storageAdapter = storageAdapter;
        this.eventProducer = eventProducer;
    }

    public AbstractWorkflowTask mapSchemaToTask(CrudWorkflowTaskSchema taskSchema) {
        AbstractCrudWorkflowTask task = null;

        switch(taskSchema.getSubtype()) {
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
        if(!taskSchema.getNeighbors().isEmpty())
            task.setNext(taskSchema.getNeighbors().get(0));

        task.setTable(taskSchema.getTable());
        task.setId(taskSchema.getId());
        task.setStorageAdapter(storageAdapter);
        task.setEventProducer(eventProducer);
        return task;
    }

    private List<RowSelectionCriterion> mapRowSelectionCriteria(CrudWorkflowTaskSchema taskSchema) {
        if(taskSchema.getRowSelectionCriteria() == null) return new ArrayList<>();
        return taskSchema.getRowSelectionCriteria().stream()
                .map(c -> new RowSelectionCriterion(c.getColumn(), c.getComparator(), c.getValue()))
                .collect(Collectors.toList());
    }

    private List<UpdateInstruction> mapUpdateInstructions(CrudWorkflowTaskSchema taskSchema) {
        if(taskSchema.getUpdateInstructions() == null) return new ArrayList<>();

        return taskSchema.getUpdateInstructions().stream()
                .map(i -> new UpdateInstruction(i.getField(), i.getOperation(), i.getValue()))
                .collect(Collectors.toList());
    }

}
