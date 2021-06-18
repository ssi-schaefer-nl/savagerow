package io.aero.v2;

import static spark.Spark.*;

import com.fasterxml.jackson.core.JacksonException;
import io.aero.v2.controller.DatabaseController;
import io.aero.v2.controller.TableRowController;
import io.aero.v2.controller.TableSchemaController;
import io.aero.v2.controller.WorkflowController;
import io.aero.v2.util.RequestParams;
import io.aero.v2.util.Workspace;
import io.aero.v2.workflowqueue.WorkflowTaskQueue;

public class SavageRow {
    private static String API_PREFIX = "/api/v1";

    public static void main(String[] args) {
        port(9010);
        WorkflowTaskQueue.getQueue().start();

        setupBefore();
        setupGetRoutes();
        setupPostRoutes();
        setupPutRoutes();
        setupDeleteRoutes();
        setupExceptions();
    }

    private static void setupBefore() {
        before(API_PREFIX + "/:database/*", (request, response) -> Workspace.setCurrentDatabase(request.params(RequestParams.Parameter.Database)));
    }


    private static void setupGetRoutes() {
        get(API_PREFIX + "/:database", DatabaseController.getAllDatabases);
        get(API_PREFIX + "/:database/database/:table", TableRowController.getRows);
        get(API_PREFIX + "/:database/database/:table/schema", TableSchemaController.getSchema);
        get(API_PREFIX + "/:database/database", DatabaseController.getTables);
        get(API_PREFIX + "/:database/workflow", WorkflowController.getSummary);
        get(API_PREFIX + "/:database/workflow/:table/:type", WorkflowController.getTableWorkflows);
        get(API_PREFIX + "/:database/workflow/all", WorkflowController.getAllWorkflows);
    }

    private static void setupPostRoutes() {
        post(API_PREFIX + "/:database", DatabaseController.createDatabase);
        post(API_PREFIX + "/:database/database/:table/column", TableSchemaController.addColumn);
        post(API_PREFIX + "/:database/database/:table", TableRowController.addRows);
        post(API_PREFIX + "/:database/workflow/:type", WorkflowController.addWorkflow);
        post(API_PREFIX + "/:database/workflow/:table/:type/:name/active/:active", WorkflowController.setActive);

    }

    private static void setupPutRoutes() {
        put(API_PREFIX + "/:database/database/:table/:row", TableRowController.updateRow);
        put(API_PREFIX + "/:database/database/:table/column/:column", TableSchemaController.renameColumn);
    }

    private static void setupDeleteRoutes() {
        delete(API_PREFIX + "/:database/database/:table/:row", TableRowController.deleteRow);
        delete(API_PREFIX + "/:database/database/:table/column/:column", TableSchemaController.deleteColumn);
        delete(API_PREFIX + "/:database/workflow/:table/:type/:name", WorkflowController.deleteWorkflow);

    }

    private static void setupExceptions() {
//        exception(JacksonException.class, (e, request, response) -> {
//            response.status(400);
//            response.body("Error parsing JSON");
//        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
