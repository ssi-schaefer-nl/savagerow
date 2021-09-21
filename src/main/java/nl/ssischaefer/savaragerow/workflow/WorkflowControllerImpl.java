package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.mapper.WorkflowMapper;
import nl.ssischaefer.savaragerow.workflow.persistence.WorkflowRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkflowControllerImpl implements WorkflowController {
    private final WorkflowService workflowService;
    private final WorkflowMapper workflowMapper;
    private final WorkflowRepository workflowRepository;

    public WorkflowControllerImpl(WorkflowService workflowService, WorkflowMapper workflowMapper, WorkflowRepository repository) {
        this.workflowService = workflowService;
        this.workflowMapper = workflowMapper;
        this.workflowRepository = repository;
    }

    @Override
    public List<WorkflowSchema> findAll() {
        return workflowRepository.getAll();
    }

    @Override
    public WorkflowSchema find(String id) {
        return workflowRepository.get(id).orElse(new WorkflowSchema());
    }

    @Override
    public void createOrUpdate(WorkflowSchema workflowSchema) {
        var previouslyEnabled = find(workflowSchema.getId()).isEnabled();
        workflowRepository.save(workflowSchema);
        startOrStopWorkflowIfNecessary(workflowSchema, previouslyEnabled);
    }

    private void startOrStopWorkflowIfNecessary(WorkflowSchema workflowSchema, boolean previouslyEnabled) {
        if (workflowSchema.isEnabled() && !previouslyEnabled) {
            var workflow = workflowMapper.createWorkflow(workflowSchema);
            if(!workflowService.startWorkflow(workflow)) {
                workflowSchema.setEnabled(false);
                workflowRepository.save(workflowSchema);
            }
        } else if (!workflowSchema.isEnabled() && previouslyEnabled) {
            workflowService.stopWorkflow(workflowSchema.getId());
        }
    }

    @Override
    public void delete(String id) {
        workflowService.stopWorkflow(id);
        workflowRepository.delete(id);
    }

    @Override
    public String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Map<String, String> getTaskInput(String workflow, Long taskId) {
        return workflowRepository.get(workflow).map(workflowMapper::createWorkflow).map(w -> w.getInput(taskId)).orElse(new HashMap<>());

    }

    @Override
    public void startAll() {
        workflowRepository.getAll().stream()
                .filter(WorkflowSchema::isEnabled)
                .map(workflowMapper::createWorkflow)
                .forEach(workflowService::startWorkflow);
    }


}
