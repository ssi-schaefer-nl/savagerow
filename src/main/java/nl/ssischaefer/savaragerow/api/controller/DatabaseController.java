package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.management.query.GetTablesQuery;
import nl.ssischaefer.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.util.Workspace;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;


public class DatabaseController {
    private final ManagementService managementService;

    public DatabaseController(ManagementService managementService) {
        this.managementService = managementService;
    }

    public void setup(String prefix) {
        post(prefix + "/:database", this::createDatabase);
        get(prefix + "/:database", this::getAllDatabases);
        get(prefix + "/:database/database", this::getTables);
        delete(prefix + "/:database", this::deleteDatabase);
    }

    public String getAllDatabases(Request request, Response response) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(Workspace.listDatabases());
    }

    public String getTables(Request request, Response response) throws SQLException, JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new GetTablesQuery().execute().getResult());
    }

    public String createDatabase(Request request, Response response) throws DatabaseException, IOException, SQLException {
        String database = request.params(RequestParams.Parameter.Database);
        managementService.createDatabase(database);
        return "";
    }

    public String deleteDatabase(Request request, Response response) throws IOException {
        Workspace.removeDatabase(request.params(RequestParams.Parameter.Database));
        return "";
    }

}
