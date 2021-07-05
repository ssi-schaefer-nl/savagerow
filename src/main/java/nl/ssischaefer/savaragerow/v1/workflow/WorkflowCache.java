package nl.ssischaefer.savaragerow.v1.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.v1.util.Workspace;
import nl.ssischaefer.savaragerow.v1.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.v1.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.v1.workflow.triggeredworkflow.TriggeredWorkflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkflowCache {
    private DocumentContext cachedJsonDocument;
    private String lastWorkspace;
    private static WorkflowCache workflowCache = null;

    public static WorkflowCache get() {
        if (workflowCache == null) {
            workflowCache = new WorkflowCache();
        }
        return workflowCache;
    }

    public WorkflowCache() {
        cachedJsonDocument = null;
        lastWorkspace = "";
    }

    public void saveDocument(DocumentContext documentContext) throws WorkspaceNotSetException, IOException {
        cachedJsonDocument = documentContext;
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        String json = mapper.writeValueAsString(mapper.readValue(documentContext.jsonString(), Object.class));

        Path path = Paths.get(Workspace.getCurrentWorkspace(), "workflows.json");
        Files.write(path, json.getBytes());

    }

    public DocumentContext getDocument() throws Exception {
        if (isRefreshCacheNecessary()) {
            refreshCachedDocument();
        }
        return cachedJsonDocument;
    }

    private void refreshCachedDocument() throws WorkspaceNotSetException, IOException {
        Path path = Paths.get(Workspace.getCurrentWorkspace(), "workflows.json");
        lastWorkspace = Workspace.getCurrentWorkspace();
        try {
            cachedJsonDocument = JsonPath.parse(Files.readString(path));
        } catch (IOException | IllegalArgumentException e) {
            cachedJsonDocument = createEmptyDocument();
        }
    }

    private DocumentContext createEmptyDocument() throws IOException, WorkspaceNotSetException {
        Map<String, Object> m = new HashMap<>();
        m.put(TriggeredWorkflow.class.getSimpleName().toLowerCase(), new ArrayList<>());
        m.put(ScheduledWorkflow.class.getSimpleName().toLowerCase(), new ArrayList<>());

        DocumentContext doc = JsonPath.parse(m);
        saveDocument(doc);

        return doc;
    }

    private boolean isRefreshCacheNecessary() throws WorkspaceNotSetException {
        String currentWorkspace = Workspace.getCurrentWorkspace();
        return cachedJsonDocument == null || !lastWorkspace.equals(currentWorkspace);
    }
}
