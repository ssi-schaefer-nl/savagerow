package nl.ssischaefer.savaragerow.v2.api.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.AbstractWorkflow;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowCache;
import nl.ssischaefer.savaragerow.v2.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowVariant;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue.WorkflowQueue;
import spark.Request;
import spark.Response;
import spark.Route;

public class WorkflowController {
    private static final WorkflowService workflowService = new WorkflowService(WorkflowCache.get(), WorkflowQueue.getQueue());

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
