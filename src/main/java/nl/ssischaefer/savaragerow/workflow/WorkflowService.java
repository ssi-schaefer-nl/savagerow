package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.PathNotFoundException;
import net.minidev.json.JSONArray;
import nl.ssischaefer.savaragerow.workflow.model.Workflow;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTask;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class WorkflowService {
    private final WorkflowDataSource dataSource;
    private static final String WORKFLOW_KEY = Workflow.class.getSimpleName().toLowerCase();

    public WorkflowService(WorkflowDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Workflow> findAll() throws Exception {
        return readWorkflows(String.format("$.%s[*]", WORKFLOW_KEY));
    }

    public List<Workflow> findByTask(WorkflowTask task) throws Exception {
        return readWorkflows(String.format("$.%s[?(@.table == '%s' && @.type == '%s')]", WORKFLOW_KEY, task.getTable(), task.getType()));
    }

    private List<Workflow> readWorkflows(String query) throws Exception {
        JSONArray read;
        try {
            read = dataSource.getDocument().read(query);
        } catch (PathNotFoundException e) {
            read = dataSource.createEmptyDocument().read(query);
        }
        return read.stream().map(r -> new ObjectMapper().configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true).convertValue(r, Workflow.class)).collect(Collectors.toList());
    }

    public void update(Workflow workflow) throws Exception {
        dataSource.getDocument().set(String.format("$.%s[?(@.identifier == '%s')]", WORKFLOW_KEY, workflow.getIdentifier()), workflow);
        dataSource.saveDocument();
    }

    public void add(Workflow workflow) throws Exception {
        workflow.setIdentifier(UUID.randomUUID().toString());
        dataSource.getDocument().add(String.format("$.%s", WORKFLOW_KEY), new ObjectMapper().convertValue(workflow, LinkedHashMap.class));
        dataSource.saveDocument();
    }

    public void delete(Workflow workflow) throws Exception {
        dataSource.getDocument().delete(String.format("$.%s[?(@.identifier == '%s')]", WORKFLOW_KEY, workflow.getIdentifier()));
        dataSource.saveDocument();
    }


}
