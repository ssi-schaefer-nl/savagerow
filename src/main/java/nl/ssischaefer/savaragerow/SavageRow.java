package nl.ssischaefer.savaragerow;

import nl.ssischaefer.savaragerow.api.controller.*;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.operations.OperationsService;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import nl.ssischaefer.savaragerow.workspace.WorkspaceService;
import nl.ssischaefer.savaragerow.workflow.WorkflowDataSource;
import nl.ssischaefer.savaragerow.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskProducer;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskQueue;

import static spark.Spark.*;

public class SavageRow {
    private static final String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        port(Configuration.parseOrDefaultInteger("PORT", 9010));
        staticFiles.location("/public");

        var workflowService = new WorkflowService(WorkflowDataSource.get());
        var managementService = new ManagementService();
        var taskQueue = WorkflowTaskQueue.initQueue(workflowService);
        var taskProducer = new WorkflowTaskProducer(taskQueue);
        var operationsService = new OperationsService(taskProducer);

        var schemaController = new SchemaController(managementService);
        var workflowController = new WorkflowController(workflowService);
        var rowController = new RowController(operationsService);
        var workspaceController = new WorkspaceController();

        workspaceController.setup(API_PREFIX);
        schemaController.setup(API_PREFIX);
        workflowController.setup(API_PREFIX);
        rowController.setup(API_PREFIX);

        setupBefore();
        setupExceptions();
    }

    private static void setupBefore() {
        before(API_PREFIX + "/:database/database/*", (request, response) -> WorkspaceService.setCurrentWorkspace(request.params(RequestParams.Parameter.Database)));
        before(API_PREFIX + "/:database/workflow", (request, response) -> WorkspaceService.setCurrentWorkspace(request.params(RequestParams.Parameter.Database)));
    }

    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
