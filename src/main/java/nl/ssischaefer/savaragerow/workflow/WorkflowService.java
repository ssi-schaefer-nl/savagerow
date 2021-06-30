package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowService {
    private final WorkflowCache workflowCache;

    public WorkflowService(WorkflowCache workflowCache) {
        this.workflowCache = workflowCache;
    }

    public List<AbstractWorkflow> find(WorkflowVariant variant) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        JSONArray read = document.read(String.format("$.%s[*]", variant.getType().getSimpleName().toLowerCase()));
        return read.stream().map(r -> new ObjectMapper().convertValue(r, variant.getType())).collect(Collectors.toList());

    }

    public void delete(AbstractWorkflow workflow) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        String query = generateQuery(workflow);
        document.delete(query);
        workflowCache.saveDocument(document);
    }

    public void add(AbstractWorkflow workflow) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        document.add(String.format("$.%s", workflow.getClass().getSimpleName().toLowerCase()), new ObjectMapper().convertValue(workflow, LinkedHashMap.class));
        workflowCache.saveDocument(document);
    }

    public void update(AbstractWorkflow workflow) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        document.set(generateQuery(workflow),  new ObjectMapper().convertValue(workflow, LinkedHashMap.class));
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
