package nl.ssischaefer.savaragerow.workflow;

import com.jayway.jsonpath.DocumentContext;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;

import java.util.List;

public class WorkflowService {
    private final WorkflowCache workflowCache;

    public WorkflowService(WorkflowCache workflowCache) {
        this.workflowCache = workflowCache;
    }

    public List<? extends AbstractWorkflow> find(Class<? extends AbstractWorkflow> workflow) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        return document.read(String.format("$.%s[*]", workflow.getSimpleName().toLowerCase()));
    }

    public void delete(AbstractWorkflow workflow) throws Exception {

        DocumentContext document = workflowCache.getDocument();
        String query = generateQuery(workflow);
        document.delete(query);
        workflowCache.saveDocument(document);
    }


    public void add(AbstractWorkflow workflow) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        document.add(String.format("$.%s", workflow.getClass().getSimpleName().toLowerCase()), workflow);
        workflowCache.saveDocument(document);
    }

    private String generateQuery(AbstractWorkflow workflow) {
        String field = workflow.getClass().getSimpleName().toLowerCase();
        if (TriggeredWorkflow.class.equals(workflow.getClass())) {
            TriggeredWorkflow tw = (TriggeredWorkflow) workflow;
            return String.format("$.%s[?(@.name == '%s' && @.type == '%s' && @.table == '%s')]", field, tw.getName(), tw.getType(), tw.getTable());
        } else {
            return String.format("$.%s[?(@.name == '%s')]", field, workflow.getName());
        }
    }
}
