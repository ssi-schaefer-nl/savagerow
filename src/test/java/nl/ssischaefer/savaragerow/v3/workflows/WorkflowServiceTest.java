package nl.ssischaefer.savaragerow.v3.workflows;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import nl.ssischaefer.savaragerow.v3.workflow.WorkflowDataSource;
import nl.ssischaefer.savaragerow.v3.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.v3.workflow.model.Workflow;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.v3.workflow.model.action.Action;
import nl.ssischaefer.savaragerow.v3.workflow.model.action.DeleteAction;
import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTask;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.*;

public class WorkflowServiceTest {
    @Mock
    private WorkflowDataSource dataSource;

    private DocumentContext createDocument() throws JsonProcessingException {
        List<Workflow> workflows = new ArrayList<>();
        List<String> types = Arrays.asList("insert", "delete", "update");
        types.forEach(t -> {
            Workflow workflow = new Workflow()
                    .setIdentifier(UUID.randomUUID().toString())
                    .setName("Workflow_"+t)
                    .setTriggerType(WorkflowTriggerType.fromString(t))
                    .setTable("Table_"+t)
                    .setActive(true)
                    .setActions(getActions());
            workflows.add(workflow);
        });
        Map<String, Object> map = new HashMap<>() {{
            put("workflow", workflows);
        }};
        return JsonPath.parse(new ObjectMapper().writeValueAsString(map));
    }

    private List<Action> getActions() {
        List<RowCriteria> criteria = Collections.singletonList(new RowCriteria().setColumn("column1").setComparator("==").setRequired("testvalue"));
        Action action1 = new DeleteAction().setRowCriteria(criteria).setTable("test").setName("action1").setStep(1).setType("delete");
        Action action2 = new DeleteAction().setRowCriteria(criteria).setTable("test").setName("action2").setStep(2).setType("delete");
        return Arrays.asList(action1, action2);
    }

    @Test
    public void shouldFindThreeWorkflows() throws Exception {
        dataSource = Mockito.mock(WorkflowDataSource.class);
        Mockito.when(dataSource.getDocument()).thenReturn(createDocument());
        WorkflowService workflowService = new WorkflowService(dataSource);
        List<Workflow> workflows = workflowService.findAll();
        Assert.assertEquals(3, workflows.size());
    }

    @Test
    public void shouldFindOneWorkflow() throws Exception {
        dataSource = Mockito.mock(WorkflowDataSource.class);
        Mockito.when(dataSource.getDocument()).thenReturn(createDocument());
        WorkflowService workflowService = new WorkflowService(dataSource);
        WorkflowTask task = new WorkflowTask().setType(WorkflowTriggerType.DELETE).setTable("Table_delete").setData(new HashMap<>());

        List<Workflow> workflows = workflowService.findByTask(task);
        Assert.assertEquals(1, workflows.size());
    }

    @Test
    public void shouldUpdateExistingWorkflow() throws Exception {
        Workflow workflow = new Workflow()
                .setIdentifier(UUID.randomUUID().toString())
                .setName("Workflow")
                .setTriggerType(WorkflowTriggerType.UPDATE)
                .setTable("Table")
                .setActive(true)
                .setActions(getActions());

        Map<String, Object> map = new HashMap<>() {{
            put("workflow", Collections.singletonList(workflow));
        }};

        dataSource = Mockito.mock(WorkflowDataSource.class);
        Mockito.when(dataSource.getDocument()).thenReturn(JsonPath.parse(new ObjectMapper().writeValueAsString(map)));
        WorkflowService workflowService = new WorkflowService(dataSource);

        List<Workflow> workflowsBeforeUpdate = workflowService.findAll();
        workflowService.update(workflow.setActive(false));
        List<Workflow> workflowsAfterUpdate = workflowService.findAll();

        Assert.assertEquals(1, workflowsBeforeUpdate.size());
        Assert.assertEquals(workflowsBeforeUpdate.size(), workflowsAfterUpdate.size());
        Assert.assertTrue(workflowsBeforeUpdate.get(0).isActive());
        Assert.assertFalse(workflowsAfterUpdate.get(0).isActive());
    }

    @Test
    public void shouldUpdateExistingWorkflowTwice() throws Exception {
        Workflow workflow = new Workflow()
                .setIdentifier(UUID.randomUUID().toString())
                .setName("Workflow")
                .setTriggerType(WorkflowTriggerType.UPDATE)
                .setTable("Table")
                .setActive(true)
                .setActions(getActions());

        Map<String, Object> map = new HashMap<>() {{
            put("workflow", Collections.singletonList(workflow));
        }};

        dataSource = Mockito.mock(WorkflowDataSource.class);
        Mockito.when(dataSource.getDocument()).thenReturn(JsonPath.parse(new ObjectMapper().writeValueAsString(map)));
        WorkflowService workflowService = new WorkflowService(dataSource);

        List<Workflow> workflowsBeforeUpdate = workflowService.findAll();
        workflowService.update(workflow.setActive(false));
        List<Workflow> workflowsAfterFirstUpdate = workflowService.findAll();

        workflowService.update(workflow.setActive(true));
        List<Workflow> workflowsAfterSecondUpdate = workflowService.findAll();

        Assert.assertEquals(1, workflowsBeforeUpdate.size());
        Assert.assertTrue(workflowsBeforeUpdate.get(0).isActive());

        Assert.assertEquals(workflowsBeforeUpdate.size(), workflowsAfterFirstUpdate.size());
        Assert.assertFalse(workflowsAfterFirstUpdate.get(0).isActive());
        Assert.assertEquals(workflowsBeforeUpdate.size(), workflowsAfterSecondUpdate.size());
        Assert.assertTrue(workflowsAfterSecondUpdate.get(0).isActive());

    }

    @Test
    public void shouldAddWorkflow() throws Exception {
        dataSource = Mockito.mock(WorkflowDataSource.class);
        Map<String, Object> map = new HashMap<>() {{
           put("workflow", new ArrayList<>());
        }};

        Mockito.when(dataSource.getDocument()).thenReturn(JsonPath.parse(new ObjectMapper().writeValueAsString(map)));

        WorkflowService workflowService = new WorkflowService(dataSource);
        Workflow workflow = new Workflow()
                .setName("Workflow")
                .setTriggerType(WorkflowTriggerType.UPDATE)
                .setTable("Table")
                .setActive(true)
                .setActions(getActions());


        Assert.assertNull(workflow.getIdentifier());
        List<Workflow> workflowsBeforeAdd = workflowService.findAll();

        workflowService.add(workflow);

        List<Workflow> workflowsAfterAdd = workflowService.findAll();

        Assert.assertTrue(workflowsBeforeAdd.isEmpty());
        Assert.assertEquals(1, workflowsAfterAdd.size());
        Assert.assertNotNull(workflowsAfterAdd.get(0).getIdentifier());
    }

    @Test
    public void shouldDeleteWorkflow() throws Exception {
        Workflow workflow = new Workflow()
                .setIdentifier(UUID.randomUUID().toString())
                .setName("Workflow")
                .setTriggerType(WorkflowTriggerType.UPDATE)
                .setTable("Table")
                .setActive(true)
                .setActions(getActions());

        Map<String, Object> map = new HashMap<>() {{
            put("workflow", Collections.singletonList(workflow));
        }};

        dataSource = Mockito.mock(WorkflowDataSource.class);
        Mockito.when(dataSource.getDocument()).thenReturn(JsonPath.parse(new ObjectMapper().writeValueAsString(map)));
        WorkflowService workflowService = new WorkflowService(dataSource);

        List<Workflow> workflowsBeforeDelete = workflowService.findAll();
        workflowService.delete(workflow);
        List<Workflow> workflowsAfterDelete = workflowService.findAll();
        Assert.assertEquals(1, workflowsBeforeDelete.size());
        Assert.assertTrue(workflowsAfterDelete.isEmpty());
    }

}
