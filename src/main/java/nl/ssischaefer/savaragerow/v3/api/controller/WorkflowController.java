package nl.ssischaefer.savaragerow.v3.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v3.workflow.model.Workflow;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowType;
import nl.ssischaefer.savaragerow.v3.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v3.util.RequestParams;
import nl.ssischaefer.savaragerow.v3.util.exception.WorkspaceNotSetException;
import spark.Request;
import spark.Response;

import java.util.List;

import static spark.Spark.*;

public class WorkflowController {

    public void setup(String prefix) {
        get(prefix + "/:database/workflow/:table/:type", this::getTableWorkflows);
        get(prefix + "/:database/workflow/all", this::getAllWorkflows);
        post(prefix + "/:database/workflow/:type", this::addWorkflow);
        post(prefix + "/:database/workflow/:table/:type/:name/active/:active", this::setActive);
        delete(prefix + "/:database/workflow/:table/:type/:name", this::deleteWorkflow);
    }

    public String getAllWorkflows(Request request, Response response) throws WorkspaceNotSetException, JsonProcessingException {
        WorkflowService workflowManager = WorkflowService.getWorkflowServiceForCurrentWorkspace();
        return new ObjectMapper().writeValueAsString(workflowManager.get());
    }

    public String getTableWorkflows(Request request, Response response) throws WorkspaceNotSetException, JsonProcessingException {
        String table = request.params(RequestParams.Parameter.Table);
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));

        List<Workflow> tableWorkflows = WorkflowService.getWorkflowServiceForCurrentWorkspace().get(type, table);
        return new ObjectMapper().writeValueAsString(tableWorkflows);
    }

    public String addWorkflow(Request request, Response response) throws Exception {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Workflow workflow = objectMapper.readValue(request.body(), Workflow.class);

        WorkflowService workflows = WorkflowService.getWorkflowServiceForCurrentWorkspace();
        workflows.add(type, workflow);
        WorkflowService.save(workflows);

        return "";
    }

    public String deleteWorkflow(Request request, Response response) throws Exception {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);
        WorkflowService workflows = WorkflowService.getWorkflowServiceForCurrentWorkspace();
        workflows.delete(type, table, name);
        WorkflowService.save(workflows);

        return "";
    }

    public String setActive(Request request, Response response) throws Exception {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);
        boolean active = Boolean.parseBoolean(request.params(RequestParams.Parameter.WorkflowActive));

        WorkflowService workflows = WorkflowService.getWorkflowServiceForCurrentWorkspace();
        workflows.setActive(type, table, name, active);
        WorkflowService.save(workflows);

        return "";
    }
}
