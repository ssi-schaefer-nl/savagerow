package nl.ssischaefer.savaragerow.v2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.query.metadata.GetTablesQuery;
import nl.ssischaefer.savaragerow.v2.service.DatabaseService;
import nl.ssischaefer.savaragerow.v2.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.util.Workspace;
import nl.ssischaefer.savaragerow.v2.util.exception.DatabaseException;
import spark.Request;
import spark.Response;
import spark.Route;


public class DatabaseController {
    private static DatabaseService databaseService = new DatabaseService();
    private DatabaseController() {
        //ignore
    }

    public static final Route getAllDatabases = (Request request, Response response) -> new ObjectMapper().writeValueAsString(Workspace.listDatabases());
    public static final Route getTables = (Request request, Response response) -> new ObjectMapper().writeValueAsString(new GetTablesQuery().execute().getResult());

    public static final Route createDatabase = (Request request, Response response) -> {
        String database = request.params(RequestParams.Parameter.Database);
        databaseService.createDatabase(database);
        return "";
    };

    public static final Route deleteDatabase = (Request request, Response response) -> {
        Workspace.removeDatabase(request.params(RequestParams.Parameter.Database));
        return "";
    };

}
