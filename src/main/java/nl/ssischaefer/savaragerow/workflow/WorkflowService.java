package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.api.dto.WorkflowID;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowMapper;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.AbstractWorkflowTrigger;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableEventObserver;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableTrigger;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class WorkflowService {
    private final WorkflowRepository repository;
    private final TableEventObserver eventObserver;
    private final WorkflowMapper mapper;

    public WorkflowService(WorkflowRepository repository, TableEventObserver eventObserver, WorkflowMapper mapper) {
        this.repository = repository;
        this.eventObserver = eventObserver;
        this.mapper = mapper;
    }

    public List<WorkflowSchema> findAll() {
        return repository.getAll();
    }

    public void update(WorkflowSchema workflowSchema) {
        repository.save(workflowSchema);
        if (Boolean.TRUE.equals(workflowSchema.getEnabled())) loadWorkflow(workflowSchema.getId());
        else eventObserver.removeTrigger(workflowSchema.getId());
    }

    public void add(WorkflowSchema workflowSchema) {
        update(workflowSchema);
    }

    private void loadWorkflow(String id) {
        repository.get(id).ifPresent(ws -> {
            if (workflowCanBeLoaded(ws)) {
                var tasks = mapper.mapSchemaToTasks(ws.getTasks());
                AbstractWorkflowTrigger trigger = mapper.mapSchemaToTrigger(ws.getTrigger(), tasks);
                trigger.setWorkflowId(ws.getId());
                if (trigger instanceof TableTrigger) eventObserver.addTrigger((TableTrigger) trigger);
            }
        });
    }

    private boolean workflowCanBeLoaded(WorkflowSchema ws) {
        return Boolean.TRUE.equals(ws.getEnabled()) && ws.getTrigger() != null && ws.getTasks() != null;
    }

    public void delete(String id) {
        repository.get(id).ifPresent(w -> {
            repository.delete(w.getId());
            if (Boolean.FALSE.equals(w.getEnabled())) eventObserver.removeTrigger(w.getId());
        });
    }

    public WorkflowID generateUniqueID() {
        var dto = new WorkflowID();
        dto.setId(UUID.randomUUID().toString());
        return dto;
    }

    public WorkflowSchema find(String id) {
        return repository.get(id).orElse(new WorkflowSchema());
    }
}
