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
        scheduledWorkflows.add(newScheduledWorkflow("four"));
        Map<String, Object> m = new HashMap<>();
        m.put(ScheduledWorkflow.class.getSimpleName().toLowerCase(), scheduledWorkflows);
        m.put(TriggeredWorkflow.class.getSimpleName().toLowerCase(), triggeredWorkflows);
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
    public void shouldReadThreeTriggeredWorkflows() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(3, workflowService.find(WorkflowVariant.TRIGGERED.getType()).size());
    }

    @Test
    public void shouldReadFourScheduledWorkflows() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(4, workflowService.find(WorkflowVariant.SCHEDULED.getType()).size());
    }

    @Test
    public void shouldRemoveOneTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED.getType()).size();

        workflowService.delete(newTriggeredWorkflow("one"));
        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED.getType()).size());
    }

    @Test
    public void shouldRemoveOneScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED.getType()).size();

        workflowService.delete(newScheduledWorkflow("one"));
        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED.getType()).size());
    }

    @Test
    public void shouldAddOneScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        ScheduledWorkflow scheduledWorkflow = newScheduledWorkflow("five");
        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED.getType()).size();

        workflowService.add(scheduledWorkflow);
        Assert.assertEquals(initialSize + 1, workflowService.find(WorkflowVariant.SCHEDULED.getType()).size());
    }

    @Test
    public void shouldAddOneTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        TriggeredWorkflow triggeredWorkflow = newTriggeredWorkflow("four");

        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED.getType()).size();
        workflowService.add(triggeredWorkflow);
        Assert.assertEquals(initialSize + 1, workflowService.find(WorkflowVariant.TRIGGERED.getType()).size());
    }

    @Test
    public void shouldDeleteDeserializedScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        System.out.println(getJsonForWorkflows().jsonString());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        AbstractWorkflow workflow = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(newScheduledWorkflow("one")), WorkflowVariant.SCHEDULED.getType());

        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED.getType()).size();
        workflowService.delete(workflow);

        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.SCHEDULED.getType()).size());
    }

    @Test
    public void shouldDeleteDeserializedTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        AbstractWorkflow workflow = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(newTriggeredWorkflow("one")), WorkflowVariant.TRIGGERED.getType());

        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED.getType()).size();
        workflowService.delete(workflow);

        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED.getType()).size());
    }
}
