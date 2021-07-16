package nl.ssischaefer.savaragerow.workflows;

import nl.ssischaefer.savaragerow.workspace.WorkspaceService;
import nl.ssischaefer.savaragerow.workflow.WorkflowDataSource;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class DataSourceTest {
    private static final String TEST_WORKSPACE = "test";

    @BeforeEach
    public void setupWorkspace() throws IOException, SQLException {
        WorkspaceService.setCurrentWorkspace(TEST_WORKSPACE);
    }

    @Test
    public void shouldReturnEmptyWorkflowListForNewWorkspace() throws Exception {
        WorkflowDataSource workflowDataSource = WorkflowDataSource.get();
        List<?> workflows = workflowDataSource.getDocument().read("workflow", List.class);
        Assert.assertTrue(workflows.isEmpty());
    }

    @AfterEach
    public void cleanupWorkspace() throws IOException {
        WorkspaceService.removeDatabase(TEST_WORKSPACE);
    }
}
