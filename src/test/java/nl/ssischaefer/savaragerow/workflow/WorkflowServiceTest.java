package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import javax.swing.text.Document;
import java.util.*;
import java.util.stream.Collectors;

public class WorkflowServiceTest {
    @Mock
    WorkflowCache mockedWorkflowCache;

    private DocumentContext getJsonForWorkflows() throws JsonProcessingException {
        List<String> names = Arrays.asList("one", "two", "three");
        List<TriggeredWorkflow> triggeredWorkflows = names.stream().map(this::newTriggeredWorkflow).collect(Collectors.toList());
        List<ScheduledWorkflow> scheduledWorkflows = names.stream().map(this::newScheduledWorkflow).collect(Collectors.toList());
        Map<String, Object> m = new HashMap<>();
        m.put("scheduled", scheduledWorkflows);
        m.put("triggered", triggeredWorkflows);
        return JsonPath.parse(new ObjectMapper().writeValueAsString(m));
    }

    private TriggeredWorkflow newTriggeredWorkflow(String name) {
        TriggeredWorkflow triggeredWorkflow = new TriggeredWorkflow();
        triggeredWorkflow.setName(name);
        triggeredWorkflow.setTable("table");
        triggeredWorkflow.setType(WorkflowType.INSERT);
        return triggeredWorkflow;
    }

    private ScheduledWorkflow newScheduledWorkflow(String name) {
        ScheduledWorkflow scheduledWorkflow = new ScheduledWorkflow();
        scheduledWorkflow.setName(name);
        return scheduledWorkflow;
    }

    @Test
    public void shouldReadThreeTriggeredWorkflows() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(3, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldReadThreeScheduledWorkflows() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(3, workflowService.find(WorkflowVariant.SCHEDULED).size());
    }

    @Test
    public void shouldRemoveOneTriggeredWorkflow() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        workflowService.delete(newTriggeredWorkflow("one"));
        Assert.assertEquals(2, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldRemoveOneScheduledWorkflow() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        workflowService.delete(newScheduledWorkflow("one"));
        Assert.assertEquals(2, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldAddOneScheduledWorkflow() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        ScheduledWorkflow scheduledWorkflow = newScheduledWorkflow("four");

        workflowService.add(scheduledWorkflow, WorkflowVariant.SCHEDULED);
        Assert.assertEquals(4, workflowService.find(WorkflowVariant.SCHEDULED).size());
    }

    @Test
    public void shouldAddOneTriggeredWorkflow() throws Exception, WorkspaceNotSetException {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        TriggeredWorkflow triggeredWorkflow = newTriggeredWorkflow("four");

        workflowService.add(triggeredWorkflow, WorkflowVariant.TRIGGERED);
        Assert.assertEquals(4, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }
}
