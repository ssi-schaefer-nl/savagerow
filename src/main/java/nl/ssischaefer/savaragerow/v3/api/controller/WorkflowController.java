package nl.ssischaefer.savaragerow.v3.api.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v3.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v3.workflow.model.Workflow;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class WorkflowController {
    private final WorkflowService workflowService;
    private final ObjectMapper mapper;

    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
        mapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public void setup(String prefix) {
        String url = prefix + "/:database/workflow";

        get(url, this::getAllWorkflows);
        post(url, this::addWorkflow);
        put(url, this::updateWorkflow);
        delete(url, this::deleteWorkflow);
    }

    private String updateWorkflow(Request request, Response response) throws Exception {
        Workflow workflow = mapper.readValue(request.body(), Workflow.class);
        workflowService.update(workflow);
        return "";
    }

    public String getAllWorkflows(Request request, Response response) throws Exception {
        return new ObjectMapper().writeValueAsString(workflowService.findAll());
    }

    public String addWorkflow(Request request, Response response) throws Exception {
        Workflow workflow = mapper.readValue(request.body(), Workflow.class);
        workflowService.add(workflow);
        return "";
    }

    public String deleteWorkflow(Request request, Response response) throws Exception {
        Workflow workflow = mapper.readValue(request.body(), Workflow.class);
        workflowService.delete(workflow);
        return "";
    }

}
