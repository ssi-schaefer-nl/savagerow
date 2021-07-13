package nl.ssischaefer.savaragerow.v3.workflows;

import nl.ssischaefer.savaragerow.v3.util.Workspace;
import nl.ssischaefer.savaragerow.v3.workflow.WorkflowDataSource;
import nl.ssischaefer.savaragerow.v3.workflow.model.Workflow;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowTriggerType;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataSourceTest {
    private static final String TEST_WORKSPACE = "test";

    @BeforeEach
    public void setupWorkspace() throws IOException, SQLException {
        Workspace.setCurrentWorkspace(TEST_WORKSPACE);
    }

    @Test
    public void shouldReturnEmptyWorkflowListForNewWorkspace() throws Exception {
        WorkflowDataSource workflowDataSource = WorkflowDataSource.get();
        List<?> workflows = workflowDataSource.getDocument().read("workflow", List.class);
        Assert.assertTrue(workflows.isEmpty());
    }

    @AfterEach
    public void cleanupWorkspace() throws IOException {
        Workspace.removeDatabase(TEST_WORKSPACE);
    }
}
