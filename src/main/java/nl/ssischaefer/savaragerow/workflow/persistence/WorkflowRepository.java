package nl.ssischaefer.savaragerow.workflow.persistence;

import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;

import java.util.List;
import java.util.Optional;

public interface WorkflowRepository {
    public boolean save(WorkflowSchema workflowSchema);
    public Optional<WorkflowSchema> get(String id);
    public boolean delete(String id);
    public List<WorkflowSchema> getAll();
}
