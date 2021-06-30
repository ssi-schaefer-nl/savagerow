package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.workflow.action.DeleteAction;
import nl.ssischaefer.savaragerow.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        triggeredWorkflow.setActions(Arrays.asList(new DeleteAction().setTable("test").setName("test").setStep(1), new DeleteAction().setTable("test").setName("test").setStep(2)));
        return triggeredWorkflow;
    }

    private ScheduledWorkflow newScheduledWorkflow(String name) {
        ScheduledWorkflow scheduledWorkflow = new ScheduledWorkflow();
        scheduledWorkflow.setActions(Arrays.asList(new DeleteAction().setTable("test").setName("test").setStep(1), new DeleteAction().setTable("test").setName("test").setStep(2)));

        scheduledWorkflow.setName(name);
        return scheduledWorkflow;
    }

    @Test
    public void shouldReadThreeTriggeredWorkflows() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(3, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldReadFourScheduledWorkflows() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        Assert.assertEquals(4, workflowService.find(WorkflowVariant.SCHEDULED).size());
    }

    @Test
    public void shouldConvertTriggeredToJSON() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        System.out.println(getJsonForWorkflows().jsonString());
        System.out.println(new ObjectMapper().writeValueAsString(workflowService.find(WorkflowVariant.TRIGGERED)));

    }

    @Test
    public void shouldRemoveOneTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED).size();

        workflowService.delete(newTriggeredWorkflow("one"));
        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldRemoveOneScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED).size();

        workflowService.delete(newScheduledWorkflow("one"));
        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldAddOneScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        ScheduledWorkflow scheduledWorkflow = newScheduledWorkflow("five");
        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED).size();

        workflowService.add(scheduledWorkflow);
        Assert.assertEquals(initialSize + 1, workflowService.find(WorkflowVariant.SCHEDULED).size());
    }

    @Test
    public void shouldAddOneTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        TriggeredWorkflow triggeredWorkflow = newTriggeredWorkflow("four");

        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED).size();
        workflowService.add(triggeredWorkflow);
        Assert.assertEquals(initialSize + 1, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldDeleteDeserializedScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        System.out.println(getJsonForWorkflows().jsonString());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        AbstractWorkflow workflow = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(newScheduledWorkflow("one")), WorkflowVariant.SCHEDULED.getType());

        int initialSize = workflowService.find(WorkflowVariant.SCHEDULED).size();
        workflowService.delete(workflow);

        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.SCHEDULED).size());
    }

    @Test
    public void shouldDeleteDeserializedTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());

        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);
        AbstractWorkflow workflow = new ObjectMapper().readValue(new ObjectMapper().writeValueAsString(newTriggeredWorkflow("one")), WorkflowVariant.TRIGGERED.getType());

        int initialSize = workflowService.find(WorkflowVariant.TRIGGERED).size();
        workflowService.delete(workflow);

        Assert.assertEquals(initialSize - 1, workflowService.find(WorkflowVariant.TRIGGERED).size());
    }

    @Test
    public void shouldEditExistingScheduledWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        String targetName = "one";
        ScheduledWorkflow updatedWorkflow = newScheduledWorkflow(targetName);
        updatedWorkflow.setActive(true);

        List<AbstractWorkflow> workflowsPrior = workflowService.find(WorkflowVariant.SCHEDULED);
        workflowService.update(updatedWorkflow);
        List<AbstractWorkflow> workflowsAfter = workflowService.find(WorkflowVariant.SCHEDULED);

        Assert.assertEquals(1, workflowsPrior.stream().filter(w -> w.getName().equals(targetName)).count());
        Assert.assertFalse(workflowsPrior.stream().filter(w -> w.getName().equals(targetName)).findFirst().get().isActive());
        Assert.assertEquals(1, workflowsAfter.stream().filter(w -> w.getName().equals(targetName)).count());
        Assert.assertTrue(workflowsAfter.stream().filter(w -> w.getName().equals(targetName)).findFirst().get().isActive());
        Assert.assertEquals(workflowsPrior.size(), workflowsAfter.size());
    }

    @Test
    public void shouldEditExistingTriggeredWorkflow() throws Exception {
        mockedWorkflowCache = Mockito.mock(WorkflowCache.class);
        Mockito.when(mockedWorkflowCache.getDocument()).thenReturn(getJsonForWorkflows());
        WorkflowService workflowService = new WorkflowService(mockedWorkflowCache);

        String targetName = "one";
        TriggeredWorkflow triggeredWorkflow = newTriggeredWorkflow(targetName);
        triggeredWorkflow.setActive(true);

        List<AbstractWorkflow> workflowsPrior = workflowService.find(WorkflowVariant.TRIGGERED);
        workflowService.update(triggeredWorkflow);
        List<AbstractWorkflow> workflowsAfter = workflowService.find(WorkflowVariant.TRIGGERED);

        Assert.assertEquals(1, workflowsPrior.stream().filter(w -> w.getName().equals(targetName)).count());
        Assert.assertFalse(workflowsPrior.stream().filter(w -> w.getName().equals(targetName)).findFirst().get().isActive());
        Assert.assertEquals(1, workflowsAfter.stream().filter(w -> w.getName().equals(targetName)).count());
        Assert.assertTrue(workflowsAfter.stream().filter(w -> w.getName().equals(targetName)).findFirst().get().isActive());
        Assert.assertEquals(workflowsPrior.size(), workflowsAfter.size());
    }
}
