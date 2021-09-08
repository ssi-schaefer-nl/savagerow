package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.common.schema.WorkflowSchema;
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
        String url = prefix + "/workflow";

        get(url, this::getAllWorkflows);
        get(url.concat("/:id"), this::getWorkflow);
        get(url.concat("/generate/id"), this::getUniqueID);
        post(url, this::addWorkflow);
        put(url, this::updateWorkflow);
        delete(url.concat("/:id"), this::deleteWorkflow);
    }

    private String getUniqueID(Request request, Response response) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(workflowService.generateUniqueID());
    }

    private String updateWorkflow(Request request, Response response) throws Exception {
        var workflowSchema = mapper.readValue(request.body(), WorkflowSchema.class);
        workflowService.update(workflowSchema);
        return "";
    }

    public String getAllWorkflows(Request request, Response response) throws Exception {
        return new ObjectMapper().writeValueAsString(workflowService.findAll());
    }

    public String addWorkflow(Request request, Response response) throws Exception {
        var workflowSchema = mapper.readValue(request.body(), WorkflowSchema.class);
        workflowService.add(workflowSchema);
        return "";
    }

    public String deleteWorkflow(Request request, Response response) {
        String id = request.params("id");
        workflowService.delete(id);
        return "";
    }


    private String getWorkflow(Request request, Response response) throws JsonProcessingException {
        String id = request.params("id");
        return new ObjectMapper().writeValueAsString(workflowService.find(id));

    }


}
