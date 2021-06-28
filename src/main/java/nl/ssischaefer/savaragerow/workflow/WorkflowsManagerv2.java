package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;

import javax.swing.text.Document;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class WorkflowsManagerv2 {
    private static DocumentContext cachedJsonDocument = null;
    private static String lastWorkspace = "";

    public static DocumentContext getDocument() throws WorkspaceNotSetException, Exception {
        if (isRefreshCacheNecessary()) {
            refreshCachedDocument();
        }
        return cachedJsonDocument;
    }

    private static void refreshCachedDocument() throws WorkspaceNotSetException, Exception {
        Path path = Paths.get(Workspace.getCurrentWorkspace(), "workflows.json");
        cachedJsonDocument = JsonPath.parse(Files.readString(path));
    }

    private static boolean isRefreshCacheNecessary() throws WorkspaceNotSetException {
        String currentWorkspace = Workspace.getCurrentWorkspace();
        return cachedJsonDocument == null || !lastWorkspace.equals(currentWorkspace);
    }


}
