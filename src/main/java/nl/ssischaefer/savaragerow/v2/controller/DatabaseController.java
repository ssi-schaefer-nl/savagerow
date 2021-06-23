package nl.ssischaefer.savaragerow.v2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.query.metadata.GetTablesQuery;
import nl.ssischaefer.savaragerow.v2.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.util.Workspace;
import spark.Request;
import spark.Response;
import spark.Route;


public class DatabaseController {
    private DatabaseController() {
        //ignore
    }

    public static final Route getAllDatabases = (Request request, Response response) -> new ObjectMapper().writeValueAsString(Workspace.listDatabases());
    public static final Route getTables = (Request request, Response response) -> new ObjectMapper().writeValueAsString(new GetTablesQuery().execute().getResult());

    public static final Route createDatabase = (Request request, Response response) -> {
        Workspace.setCurrentWorkspace(request.params(RequestParams.Parameter.Database));
        return "";
    };

    public static final Route deleteDatabase = (Request request, Response response) -> {
        Workspace.removeDatabase(request.params(RequestParams.Parameter.Database));
        return "";
    };

}
