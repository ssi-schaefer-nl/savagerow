package nl.ssischaefer.savaragerow.workflow;

import com.jayway.jsonpath.DocumentContext;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;

import java.util.List;

public class WorkflowService {
    private WorkflowCache workflowCache;

    public WorkflowService(WorkflowCache workflowCache) {
        this.workflowCache = workflowCache;
    }

    public List<? extends AbstractWorkflow> find(WorkflowVariant variant) throws Exception, WorkspaceNotSetException {
        DocumentContext document = workflowCache.getDocument();
        return document.read(String.format("$.%s[*]", variant.getName()));
    }

    public void delete(TriggeredWorkflow triggeredWorkflow) throws Exception, WorkspaceNotSetException {
        DocumentContext document = workflowCache.getDocument();
        String query = String.format("$.triggered[?(@.name == '%s' && @.type == '%s' && @.table == '%s')]", triggeredWorkflow.getName(), triggeredWorkflow.getType(), triggeredWorkflow.getTable());
        document.delete(query);
    }

    public void delete(ScheduledWorkflow scheduledWorkflow) throws Exception, WorkspaceNotSetException {
        DocumentContext document = workflowCache.getDocument();
        String query = String.format("$.triggered[?(@.name == '%s')]", scheduledWorkflow.getName());
        document.delete(query);
    }

    public void add(AbstractWorkflow workflow, WorkflowVariant variant) throws Exception, WorkspaceNotSetException {
        DocumentContext document = workflowCache.getDocument();
        document.add(String.format("$.%s", variant.getName()), workflow);
    }
}
