package nl.ssischaefer.savaragerow.v2.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import net.minidev.json.JSONArray;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.AbstractWorkflow;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowCache;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowVariant;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.triggeredworkflow.TriggeredWorkflow;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.triggeredworkflow.WorkflowType;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue.WorkflowQueue;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue.WorkflowTask;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WorkflowService {
    private final WorkflowCache workflowCache;
    private final WorkflowQueue workflowQueue;

    public WorkflowService(WorkflowCache workflowCache, WorkflowQueue workflowQueue) {
        this.workflowCache = workflowCache;
        this.workflowQueue = workflowQueue;
    }

    public void feedToQueue(Map<String, String> row, WorkflowSearchCriteria criteria) throws Exception {
        findScheduled(nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowVariant.TRIGGERED, criteria).forEach(w -> workflowQueue.feed(new WorkflowTask().setData(row).setWorkflow(w)));
    }

    public List<AbstractWorkflow> find(WorkflowSearchCriteria criteria) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        String query = criteria.generate();
        JSONArray read = document.read(query);
        return read.stream().map(r -> new ObjectMapper().convertValue(r, variant.getType())).collect(Collectors.toList());
    }

    public List<AbstractWorkflow> findScheduled(WorkflowVariant variant, WorkflowSearchCriteria criteria) throws Exception {
        DocumentContext document = workflowCache.getDocument();
        JSONArray read = document.read(String.format("$.%s%s", variant.getType().getSimpleName().toLowerCase(), criteria.generate()));
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
        String criteria;

        if (TriggeredWorkflow.class.equals(workflow.getClass())) {
            TriggeredWorkflow tw = (TriggeredWorkflow) workflow;
            criteria = new WorkflowSearchCriteria().setName(tw.getName()).setTable(tw.getTable()).setType(tw.getType()).generate();
        } else {
            criteria = new WorkflowSearchCriteria().setName(workflow.getName()).generate();
        }
        return String.format("$.%s%s", field, criteria);

    }

    public static class WorkflowSearchCriteria {
        private String table;
        private String name;
        private WorkflowType type;

        public WorkflowSearchCriteria setTable(String table) {
            this.table = table;
            return this;
        }

        public WorkflowSearchCriteria setName(String name) {
            this.name = name;
            return this;
        }

        public WorkflowSearchCriteria setType(WorkflowType type) {
            this.type = type;
            return this;
        }

        public String generate() {
            List<String> strings = generateCriteria();
            if(strings.isEmpty()) return "[*]";
            return String.format("[?(%s)]", String.join(" && ", strings));
        }

        private List<String> generateCriteria() {
            List<String> criteria = new ArrayList<>();
            if(name != null) criteria.add(generateForField("name", name));
            if(table != null) criteria.add(generateForField("table", table));
            if(type != null) criteria.add(generateForField("type", type.getType().toUpperCase()));
            return criteria;
        }

        private String generateForField(String field, String value) {
            return String.format("@.%s == '%s'", field, value);
        }
    }
}
