package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.data.common.model.SQLColumn;
import nl.ssischaefer.savaragerow.data.common.sql.SQLiteDatatype;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.management.query.CreateTableQuery;
import nl.ssischaefer.savaragerow.data.management.query.DeleteTableQuery;
import nl.ssischaefer.savaragerow.data.management.query.GetTablesQuery;
import nl.ssischaefer.savaragerow.data.management.query.RenameColumnQuery;
import nl.ssischaefer.savaragerow.api.dto.TableSchemaDTO;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class SchemaController {
    private final ManagementService managementService;

    public SchemaController(ManagementService managementService) {
        this.managementService = managementService;
    }

    public void setup(String prefix) {
        get(prefix + "/:database/database/:table/schema", this::getSchema);
        post(prefix + "/:database/database/:table", this::addTable);
        post(prefix + "/:database/database/:table/column", this::addColumn);
        put(prefix + "/:database/database/:table/column/:column", this::renameColumn);
        delete(prefix + "/:database/database/:table", this::deleteTable);
        delete(prefix + "/:database/database/:table/column/:column", this::deleteColumn);
        get(prefix + "/:database/database/tables", this::getTables); }



    public String getSchema(Request request, Response response) throws SQLException, JsonProcessingException {
        String table = request.params(RequestParams.Parameter.Table);
        var tableSchemaDTO = new TableSchemaDTO().setName(table).setColumns(managementService.getColumnsForTable(table));
        return new ObjectMapper().writeValueAsString(tableSchemaDTO);
    }

    public String addColumn(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        var objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        SQLColumn column = objectMapper.readValue(request.body(), SQLColumn.class);

        List<SQLColumn> columns = managementService.getColumnsForTable(table);
        columns.add(column);

        managementService.recreateTable(table, columns);
        return "";
    }

    public String deleteColumn(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);

        List<SQLColumn> columns = managementService.getColumnsForTable(table).stream().filter(c -> !c.getName().equals(column)).collect(Collectors.toList());
        managementService.recreateTable(table, columns);
        return "";
    }

    public String deleteTable(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        new DeleteTableQuery().setTable(table).executeUpdate();
        return "";
    }

    public String renameColumn(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);
        SQLColumn columnSchema = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), SQLColumn.class);

        new RenameColumnQuery().setTable(table).setFrom(column).setTo(columnSchema.getName()).executeQuery();
        return "";
    }

    public String addTable(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        SQLColumn columnSchema = new SQLColumn().setName("id").setDatatype(SQLiteDatatype.Integer).setNullable(false).setPk(true);
        new CreateTableQuery().setTableName(table).setColumns(Collections.singletonList(columnSchema)).executeUpdate();
        return "";
    }

    public String getTables(Request request, Response response) throws SQLException, JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new GetTablesQuery().execute().getResult());
    }

}
