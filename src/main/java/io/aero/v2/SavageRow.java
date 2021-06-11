package io.aero.v2;

import static spark.Spark.*;

import io.aero.v2.controller.DatabaseController;
import io.aero.v2.controller.TableRowController;
import io.aero.v2.controller.TableSchemaController;
import io.aero.v2.controller.WorkflowController;
import io.aero.v2.util.Path;
import io.aero.v2.util.RequestParams;
import io.aero.v2.util.Workspace;
import io.aero.v2.workflowqueue.WorkflowTaskQueue;

public class SavageRow {
    private static TableRowController rowController;

    public static void main(String[] args) {
        port(9010);

        WorkflowTaskQueue taskQueue = new WorkflowTaskQueue(2);
        taskQueue.start();

        rowController = new TableRowController(taskQueue);

        setupBefore();
        setupGetRoutes();
        setupPostRoutes();
        setupPutRoutes();
        setupDeleteRoutes();
        setupExceptions();
    }

    private static void setupBefore() {
        before(Path.Wildcard.ALL, (request, response) -> Workspace.setCurrentDatabase(request.params(RequestParams.Parameter.Database)));
    }


    private static void setupGetRoutes() {
        get(Path.Database.DATABASES, DatabaseController.getAllDatabases);
        get(Path.Database.ROWS, rowController::getRows);
        get(Path.Database.SCHEMA, TableSchemaController.getSchema);
        get(Path.Database.TABLES, DatabaseController.getTables);
        get(Path.Workflow.DB_SUMMARY, WorkflowController.getDbSummary);
        get(Path.Workflow.TABLE_TYPE, WorkflowController.getTableWorkflows);
    }

    private static void setupPostRoutes() {
        post(Path.Database.DATABASE, DatabaseController.createDatabase);
        post(Path.Database.COLUMN, TableSchemaController.addColumn);
        post(Path.Database.ROWS, rowController::addRows);
        post(Path.Workflow.TYPE, WorkflowController.addWorkflow);
        post(Path.Workflow.WORKFLOW_ACTIVE, WorkflowController.setActive);

    }

    private static void setupPutRoutes() {
        put(Path.Database.ROWS_ID, rowController::updateRow);
        put(Path.Database.COLUMN_NAME, TableSchemaController.renameColumn);
    }

    private static void setupDeleteRoutes() {
        delete(Path.Database.ROWS_ID, rowController::deleteRow);
        delete(Path.Database.COLUMN_NAME, TableSchemaController.deleteColumn);
        delete(Path.Workflow.WORKFLOW, WorkflowController.deleteWorkflow);

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
