package nl.ssischaefer.savaragerow;

import static spark.Spark.*;

import nl.ssischaefer.savaragerow.controller.DatabaseController;
import nl.ssischaefer.savaragerow.controller.RowController;
import nl.ssischaefer.savaragerow.controller.SchemaController;
import nl.ssischaefer.savaragerow.controller.WorkflowController;
import nl.ssischaefer.savaragerow.util.Configuration;
import nl.ssischaefer.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.workflow.workflowqueue.WorkflowTaskQueue;

public class SavageRow {
    private static final String API_PREFIX = "/api/v1";

    public static void main(String[] args) {

        port(Configuration.parseOrDefaultInteger("PORT", 9010));

        WorkflowTaskQueue.getQueue().start();
        staticFiles.location("/public");
        setupBefore();
        setupGetRoutes();
        setupPostRoutes();
        setupPutRoutes();
        setupDeleteRoutes();

        setupExceptions();
    }

    private static void setupBefore() {
        before(API_PREFIX + "/:database/*", (request, response) -> Workspace.setCurrentWorkspace(request.params(RequestParams.Parameter.Database)));
    }


    private static void setupGetRoutes() {
        get(API_PREFIX + "/:database", DatabaseController.getAllDatabases);
        get(API_PREFIX + "/:database/database/:table/rows", RowController.getRows);
        get(API_PREFIX + "/:database/database/:table/schema", SchemaController.getSchema);
        get(API_PREFIX + "/:database/database", DatabaseController.getTables);
        get(API_PREFIX + "/:database/workflow/:variant/all", WorkflowController.getAllWorkflows);
    }

    private static void setupPostRoutes() {
        post(API_PREFIX + "/:database", DatabaseController.createDatabase);
        post(API_PREFIX + "/:database/database/:table", SchemaController.addTable);
        post(API_PREFIX + "/:database/database/:table/column", SchemaController.addColumn);
        post(API_PREFIX + "/:database/database/:table/rows", RowController.addRows);
        post(API_PREFIX + "/:database/workflow/:variant", WorkflowController.addWorkflow);
        post(API_PREFIX + "/:database/workflow/:table/:type/:name/active/:active", WorkflowController.setActive);

    }

    private static void setupPutRoutes() {
        put(API_PREFIX + "/:database/database/:table/rows/:row", RowController.updateRow);
        put(API_PREFIX + "/:database/database/:table/column/:column", SchemaController.renameColumn);
    }

    private static void setupDeleteRoutes() {
        delete(API_PREFIX + "/:database", DatabaseController.deleteDatabase);
        delete(API_PREFIX + "/:database/database/:table", SchemaController.deleteTable);
        delete(API_PREFIX + "/:database/database/:table/rows/:row", RowController.deleteRow);
        delete(API_PREFIX + "/:database/database/:table/column/:column", SchemaController.deleteColumn);
        delete(API_PREFIX + "/:database/workflow/:variant", WorkflowController.deleteWorkflow);

    }


    private static void setupExceptions() {
        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
