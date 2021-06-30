package nl.ssischaefer.savaragerow.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.workflow.AbstractWorkflow;
import nl.ssischaefer.savaragerow.workflow.WorkflowCache;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.workflow.WorkflowVariant;
import spark.Request;
import spark.Response;
import spark.Route;

public class WorkflowController {
    private static final WorkflowService workflowService = new WorkflowService(new WorkflowCache());

    private WorkflowController() {
    }


    public static final Route getAllWorkflows = (Request request, Response response) -> {
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));
        if (variant != null) {
            try {
                return new ObjectMapper().writeValueAsString(workflowService.find(variant));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    };

    public static final Route addWorkflow = (Request request, Response response) -> {
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));

        if (variant != null) {
            AbstractWorkflow workflow = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), variant.getType());
            workflowService.add(workflow);
        }
        return "";
    };

    public static final Route updateWorkflow = (Request request, Response response) -> {
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));

        if (variant != null) {
            AbstractWorkflow workflow = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), variant.getType());
            workflowService.update(workflow);
        }
        return "";
    };


    public static final Route deleteWorkflow = (Request request, Response response) -> {
        WorkflowVariant variant = WorkflowVariant.fromString(request.params(RequestParams.Parameter.WorkflowVariant));
        System.out.println(variant);
        if (variant != null) {
            AbstractWorkflow workflow = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), variant.getType());
            workflowService.delete(workflow);
        }

        return "";
    };

}
