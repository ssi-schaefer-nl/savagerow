package nl.ssischaefer.savaragerow.workflow;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.internal.JsonFormatter;
import nl.ssischaefer.savaragerow.workspace.WorkspaceService;
import nl.ssischaefer.savaragerow.workspace.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.model.Workflow;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorkflowDataSource {
    private static final String FILE_NAME = "workflows.json";

    private DocumentContext cachedJsonDocument;
    private String lastWorkspace;
    private static WorkflowDataSource workflowDataSource = null;

    private WorkflowDataSource() {
        cachedJsonDocument = null;
        lastWorkspace = "";
    }

    public static WorkflowDataSource get() {
        if (workflowDataSource == null) {
            workflowDataSource = new WorkflowDataSource();
        }
        return workflowDataSource;
    }

    public DocumentContext getDocument() throws Exception {
        if (isRefreshCacheNecessary()) {
            refreshCachedDocument();
        }
        return cachedJsonDocument;
    }

    private boolean isRefreshCacheNecessary() throws WorkspaceNotSetException {
        String currentWorkspace = WorkspaceService.getCurrentWorkspace();
        return cachedJsonDocument == null || !lastWorkspace.equals(currentWorkspace);
    }

    private void refreshCachedDocument() throws WorkspaceNotSetException, IOException {
        var path = Paths.get(WorkspaceService.getCurrentWorkspace(), FILE_NAME);
        lastWorkspace = WorkspaceService.getCurrentWorkspace();
        try {
            cachedJsonDocument = JsonPath.parse(Files.readString(path));
        } catch (IOException | IllegalArgumentException e) {
            cachedJsonDocument = createEmptyDocument();
        }
    }

    public DocumentContext createEmptyDocument() throws IOException, WorkspaceNotSetException {
        Map<String, Object> m = new HashMap<>();
        m.put(Workflow.class.getSimpleName().toLowerCase(), new ArrayList<>());

        DocumentContext doc = JsonPath.parse(m);
        cachedJsonDocument = doc;
        saveDocument();

        return doc;
    }

    public void saveDocument() throws WorkspaceNotSetException, IOException {
        String json = JsonFormatter.prettyPrint(cachedJsonDocument.jsonString());
        Path path = Paths.get(WorkspaceService.getCurrentWorkspace(), FILE_NAME);
        Files.write(path, json.getBytes());
        refreshCachedDocument();
    }
}
