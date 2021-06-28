package nl.ssischaefer.savaragerow.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.*;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;
import spark.Request;
import spark.Response;
import spark.Route;

public class WorkflowController {
    private static WorkflowService workflowService = new WorkflowService(new WorkflowCache());

    private WorkflowController() {
    }


    public static final Route getAllWorkflows = (Request request, Response response) -> {
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));
        if (variant != null) {
            try {
                return new ObjectMapper().writeValueAsString(workflowService.find(variant));
            } catch (WorkspaceNotSetException e) {
                e.printStackTrace();
            }
        }
        return "";
    };

    public static final Route addWorkflow = (Request request, Response response) -> {
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));

        if (variant != null) {
            AbstractWorkflow workflow = objectMapper.readValue(request.body(), variant.getType());
            try {
                workflowService.add(workflow, variant);
            } catch (WorkspaceNotSetException e) {
                e.printStackTrace();
            }
        }


        return "";
    };

    public static final Route deleteWorkflow = (Request request, Response response) -> {
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));

        if (variant != null) {
            AbstractWorkflow workflow = objectMapper.readValue(request.body(), variant.getType());
            workflowService.delete(workflow);
        }

        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);
        try {
            WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
            workflows.delete(type, table, name);
            WorkflowsManager.save(workflows);
        } catch (WorkspaceNotSetException e) {
            e.printStackTrace();
        }

        return "";
    };

    public static final Route setActive = (Request request, Response response) -> {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));
        String name = request.params(RequestParams.Parameter.WorklfowName);
        String table = request.params(RequestParams.Parameter.Table);
        boolean active = Boolean.parseBoolean(request.params(RequestParams.Parameter.WorkflowActive));

        try {
            WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
            workflows.setActive(type, table, name, active);
            WorkflowsManager.save(workflows);
        } catch (WorkspaceNotSetException e) {
            e.printStackTrace();
        }

        return "";
    };

}
