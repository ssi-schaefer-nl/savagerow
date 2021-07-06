package nl.ssischaefer.savaragerow.v3;

import nl.ssischaefer.savaragerow.v3.workflow.workflowqueue.WorkflowTaskQueue;
import nl.ssischaefer.savaragerow.v3.api.controller.DatabaseController;
import nl.ssischaefer.savaragerow.v3.api.controller.RowController;
import nl.ssischaefer.savaragerow.v3.api.controller.SchemaController;
import nl.ssischaefer.savaragerow.v3.api.controller.WorkflowController;
import nl.ssischaefer.savaragerow.v3.data.management.ManagementService;
import nl.ssischaefer.savaragerow.v3.data.operations.OperationsService;
import nl.ssischaefer.savaragerow.v3.util.RequestParams;
import nl.ssischaefer.savaragerow.v3.util.Workspace;

import static nl.ssischaefer.savaragerow.v3.util.Configuration.parseOrDefaultInteger;
import static spark.Spark.*;

public class SavageRow {
    private static final String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        port(parseOrDefaultInteger("PORT", 9010));
        staticFiles.location("/public");

        ManagementService managementService = new ManagementService();
        OperationsService operationsService = new OperationsService();

        DatabaseController databaseController = new DatabaseController(managementService);
        SchemaController schemaController = new SchemaController(managementService);
        WorkflowController workflowController = new WorkflowController();
        RowController rowController = new RowController(operationsService);

        databaseController.setup(API_PREFIX);
        schemaController.setup(API_PREFIX);
        workflowController.setup(API_PREFIX);
        rowController.setup(API_PREFIX);

        WorkflowTaskQueue.getQueue().start();

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
