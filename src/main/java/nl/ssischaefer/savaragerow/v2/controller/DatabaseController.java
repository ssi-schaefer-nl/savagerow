package nl.ssischaefer.savaragerow.v2.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.ssischaefer.savaragerow.v2.dto.CreateDatabaseDTO;
import nl.ssischaefer.savaragerow.v2.dto.CreateTableDTO;
import nl.ssischaefer.savaragerow.v2.query.CreateTableQuery;
import nl.ssischaefer.savaragerow.v2.query.GetTablesQuery;
import nl.ssischaefer.savaragerow.v2.util.Workspace;
import spark.Request;
import spark.Response;
import spark.Route;


public class DatabaseController {
    private DatabaseController() {
    }

    public static final Route getAllDatabases = (Request request, Response response) -> new ObjectMapper().writeValueAsString(Workspace.listDatabases());
    public static final Route getTables = (Request request, Response response) -> new ObjectMapper().writeValueAsString(new GetTablesQuery().execute().getResult());

    public static final Route createDatabase = (Request request, Response response) -> {
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        CreateDatabaseDTO createDatabaseRequest = objectMapper.readValue(request.body(), CreateDatabaseDTO.class);

        Workspace.setCurrentDatabase(createDatabaseRequest.getDatabaseName());

        CreateTableQuery createTableSQLStatement = new CreateTableQuery();
        for (CreateTableDTO table : createDatabaseRequest.getTables()) {
            createTableSQLStatement
                    .setTableName(table.getTableName())
                    .setColumns(table.getColumns())
                    .generate()
                    .execute();
        }
        return "";
    };


}
