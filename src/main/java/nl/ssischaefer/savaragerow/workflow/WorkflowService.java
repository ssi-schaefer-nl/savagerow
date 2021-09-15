package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.api.dto.WorkflowID;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowMapper;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.AbstractWorkflowTrigger;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableEventObserver;
import nl.ssischaefer.savaragerow.workflow.model.workflowtrigger.TableTrigger;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class WorkflowService {
    private static final Logger logger = LoggerFactory.getLogger("WorkflowService");

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
                startWorkflow(ws);
            }
        });
    }

    private void startWorkflow(WorkflowSchema ws) {
        var tasks = mapper.mapWorkflowSchemaToTasks(ws);
        AbstractWorkflowTrigger trigger = mapper.mapWorkflowSchemaToTrigger(ws, tasks);
        trigger.setWorkflowId(ws.getId());
        if (trigger instanceof TableTrigger) eventObserver.addTrigger((TableTrigger) trigger);
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

    public Map<String, String> getInputForTask(String workflow, Long id) {
        WorkflowSchema workflowSchema = find(workflow);
        var triggerSchema = workflowSchema.getTrigger();
        if(triggerSchema != null && triggerSchema.getTask().equals(id)) {
            var trigger = mapper.mapWorkflowSchemaToTrigger(workflowSchema, Collections.emptyList());
            return trigger.getOutput();
        }
        var tasks = workflowSchema.getTasks();
        if(tasks == null) return new HashMap<>();

        AbstractWorkflowTask task = tasks.stream()
                .filter(t -> t.getNext() != null && t.getNext().equals(id))
                .findFirst()
                .map(mapper::mapSchemaToTask)
                .orElse(null);
        if(task == null)  return new HashMap<>();

        return task.getOutput();
    }

    public void startAll() {
        var enabledWorkflows = repository.getAll().stream().filter(this::workflowCanBeLoaded).collect(Collectors.toList());
        logger.info(String.format("Starting all (%s) enabled workflows", enabledWorkflows.size()));
        enabledWorkflows.forEach(this::startWorkflow);
    }
}
