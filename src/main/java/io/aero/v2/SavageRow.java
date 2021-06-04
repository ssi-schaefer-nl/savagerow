package io.aero.v2;

import static spark.Spark.*;

import com.fasterxml.jackson.core.JacksonException;
import io.aero.v2.controller.DatabaseController;
import io.aero.v2.controller.TableRowController;
import io.aero.v2.controller.TableSchemaController;
import io.aero.v2.controller.WorkflowController;
import io.aero.v2.util.Path;
import io.aero.v2.util.RequestParams;
import io.aero.v2.util.Workspace;

public class SavageRow {

    public static void main(String[] args) {
        port(9010);

        setupBefore();
        setupGetRoutes();
        setupPostRoutes();
        setupPutRoutes();
        setupDeleteRoutes();
        setupExceptions();
    }

    private static void setupBefore() {
        before("/", (request, response) -> {
            response.type("application/json");
        });

        before(Path.Wildcard.ALL, (request, response) -> {
            String database = request.params(RequestParams.Parameter.Database);
            Workspace.setCurrentDatabase(database);
        });
    }


    private static void setupGetRoutes() {
        get(Path.Database.DATABASES, DatabaseController.getAllDatabases);
        get(Path.Database.ROWS, TableRowController.getRows);
        get(Path.Database.SCHEMA, TableSchemaController.getSchema);
        get(Path.Database.TABLES, DatabaseController.getTables);
        get(Path.Workflow.DB_SUMMARY, WorkflowController.getDbSummary);
        get(Path.Workflow.TABLE_TYPE, WorkflowController.getTableWorkflows);
    }

    private static void setupPostRoutes() {
        post(Path.Database.DATABASE, DatabaseController.createDatabase);
        post(Path.Database.COLUMN, TableSchemaController.addColumn);
        post(Path.Database.ROWS, TableRowController.addRows);
        post(Path.Workflow.TYPE, WorkflowController.addWorkflow);
    }

    private static void setupPutRoutes() {
        put(Path.Database.ROWS_ID, TableRowController.updateRow);
        put(Path.Database.COLUMN_NAME, TableSchemaController.renameColumn);
    }

    private static void setupDeleteRoutes() {
        delete(Path.Database.ROWS, TableRowController.deleteRow);
        delete(Path.Database.COLUMN, TableSchemaController.deleteColumn);
    }

    private static void setupExceptions() {
//        exception(JacksonException.class, (e, request, response) -> {
//            response.status(400);
//            response.body("Error parsing JSON");
//
//        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
