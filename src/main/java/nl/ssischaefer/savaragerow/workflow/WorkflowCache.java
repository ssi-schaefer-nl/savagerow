package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkflowCache {
    private DocumentContext cachedJsonDocument;
    private String lastWorkspace;

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

    private void refreshCachedDocument() throws Exception {
        Path path = Paths.get(Workspace.getCurrentWorkspace(), "workflows.json");
        lastWorkspace =  Workspace.getCurrentWorkspace();
        cachedJsonDocument = JsonPath.parse(Files.readString(path));
    }

    private boolean isRefreshCacheNecessary() throws WorkspaceNotSetException {
        String currentWorkspace = Workspace.getCurrentWorkspace();
        return cachedJsonDocument == null || !lastWorkspace.equals(currentWorkspace);
    }
}
