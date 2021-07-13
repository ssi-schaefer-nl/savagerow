package nl.ssischaefer.savaragerow;

import nl.ssischaefer.savaragerow.api.controller.DatabaseController;
import nl.ssischaefer.savaragerow.api.controller.RowController;
import nl.ssischaefer.savaragerow.api.controller.SchemaController;
import nl.ssischaefer.savaragerow.api.controller.WorkflowController;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.operations.OperationsService;
import nl.ssischaefer.savaragerow.util.Configuration;
import nl.ssischaefer.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.util.Workspace;
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

        WorkflowService workflowService = new WorkflowService(WorkflowDataSource.get());
        ManagementService managementService = new ManagementService();
        WorkflowTaskQueue taskQueue = WorkflowTaskQueue.initQueue(workflowService);
        WorkflowTaskProducer taskProducer = new WorkflowTaskProducer(taskQueue);
        OperationsService operationsService = new OperationsService(taskProducer);

        DatabaseController databaseController = new DatabaseController(managementService);
        SchemaController schemaController = new SchemaController(managementService);
        WorkflowController workflowController = new WorkflowController(workflowService);
        RowController rowController = new RowController(operationsService);

        databaseController.setup(API_PREFIX);
        schemaController.setup(API_PREFIX);
        workflowController.setup(API_PREFIX);
        rowController.setup(API_PREFIX);

        setupBefore();
        setupExceptions();
    }

    private static void setupBefore() {
        before(API_PREFIX + "/:database/*", (request, response) -> Workspace.setCurrentWorkspace(request.params(RequestParams.Parameter.Database)));
    }

    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
