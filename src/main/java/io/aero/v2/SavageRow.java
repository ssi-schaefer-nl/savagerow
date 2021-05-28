package io.aero.v2;

import static spark.Spark.*;
import com.fasterxml.jackson.core.JacksonException;
import io.aero.v2.controller.DatabaseController;
import io.aero.v2.controller.TableRowController;
import io.aero.v2.controller.TableSchemaController;
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
        before(Path.Wildcard.DATABASE, (request, response) -> {
            String database = request.params(RequestParams.Parameter.Database);
            Workspace.setCurrentDatabase(database);
        });

        before(Path.Get.Tables, (request, response) -> {
            String database = request.params(RequestParams.Parameter.Database);
            Workspace.setCurrentDatabase(database);
        });
    }


    private static void setupGetRoutes() {
        get(Path.Get.DATABASES, DatabaseController.getAllDatabases);
        get(Path.Get.ROWS, TableRowController.getRows);
        get(Path.Get.SCHEMA, TableSchemaController.getSchema);
        get(Path.Get.Tables, DatabaseController.getTables);
    }

    private static void setupPostRoutes() {
        post(Path.Post.DATABASE, DatabaseController.createDatabase);
        post(Path.Post.COLUMN, TableSchemaController.addColumn);
        post(Path.Post.ROWS, TableRowController.addRows);
    }

    private static void setupPutRoutes() {
        put(Path.Put.ROWS, TableRowController.updateRow);
    }

    private static void setupDeleteRoutes() {
        delete(Path.Delete.ROWS, TableRowController.deleteRow);
    }

    private static void setupExceptions() {
        exception(JacksonException.class, (e, request, response) -> {
            response.status(400);
            System.out.println(e);
            response.body("Error parsing JSON");

        });

        exception(Exception.class, (e, request, response) -> {
            response.status(500);
            response.body(e.getMessage());
        });
    }

}
