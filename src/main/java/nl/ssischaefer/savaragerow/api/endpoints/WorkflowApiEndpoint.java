package nl.ssischaefer.savaragerow.api.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.dto.WorkflowID;
import nl.ssischaefer.savaragerow.shared.schema.WorkflowSchema;
import nl.ssischaefer.savaragerow.workflow.WorkflowController;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class WorkflowApiEndpoint {
    private final Logger logger = LoggerFactory.getLogger("WorkflowController");

    private final WorkflowController workflowController;
    private final ObjectMapper mapper;

    public WorkflowApiEndpoint(WorkflowController workflowController) {
        this.workflowController = workflowController;
        mapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
    }

    public void setup(String prefix) {
        String url = prefix + "/workflow";
        logger.info("Setting up Workflow Controller routes with prefix " + url);

        get(url, this::getAllWorkflows);
        get(url.concat("/:id"), this::getWorkflow);
        get(url.concat("/:id/:task/input"), this::getTaskInput);
        get(url.concat("/generate/id"), this::getUniqueID);
        post(url, this::addWorkflow);
        put(url, this::updateWorkflow);
        delete(url.concat("/:id"), this::deleteWorkflow);
    }

    private String getTaskInput(Request request, Response response) throws JsonProcessingException {
        String id = request.params("id");
        String task = request.params("task");
        return new ObjectMapper().writeValueAsString(workflowController.getTaskInput(id, Long.valueOf(task)));
    }

    private String getUniqueID(Request request, Response response) throws JsonProcessingException {
        var wid = new WorkflowID(workflowController.generateUniqueID());
        return new ObjectMapper().writeValueAsString(wid);
    }

    private String updateWorkflow(Request request, Response response) throws Exception {
        var workflowSchema = mapper.readValue(request.body(), WorkflowSchema.class);
        workflowController.createOrUpdate(workflowSchema);
        return "";
    }

    public String getAllWorkflows(Request request, Response response) throws Exception {
        return new ObjectMapper().writeValueAsString(workflowController.findAll());
    }

    public String addWorkflow(Request request, Response response) throws Exception {
        var workflowSchema = mapper.readValue(request.body(), WorkflowSchema.class);
        workflowController.createOrUpdate(workflowSchema);
        return "";
    }

    public String deleteWorkflow(Request request, Response response) {
        String id = request.params("id");
        workflowController.delete(id);
        return "";
    }


    private String getWorkflow(Request request, Response response) throws JsonProcessingException {
        String id = request.params("id");
        return new ObjectMapper().writeValueAsString(workflowController.find(id));

    }


}
