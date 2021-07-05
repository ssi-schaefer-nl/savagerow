package nl.ssischaefer.savaragerow.v2.api.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.api.dto.SQLColumn;
import nl.ssischaefer.savaragerow.v2.api.dto.TableSchemaDTO;
import nl.ssischaefer.savaragerow.v2.savaragerow.query.TableService;
import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.CreateTableQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.DeleteTableQuery;
import nl.ssischaefer.savaragerow.v2.data.management.query.ddl.RenameColumnQuery;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.sql.SQLiteDatatype;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SchemaController {
    private static TableService tableService = new TableService();

    private SchemaController() {
    }

    public static final Route getSchema = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        TableSchemaDTO tableSchemaDTO = new TableSchemaDTO().setName(table).setColumns(tableService.getColumnsForTable(table));
        return new ObjectMapper().writeValueAsString(tableSchemaDTO);
    };

    public static final Route addColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        SQLColumn column = objectMapper.readValue(request.body(), SQLColumn.class);

        List<SQLColumn> columns = tableService.getColumnsForTable(table);
        columns.add(column);

        tableService.recreateTable(table, columns);
        return "";
    };

    public static final Route deleteColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);

        List<SQLColumn> columns = tableService.getColumnsForTable(table).stream().filter(c -> !c.getName().equals(column)).collect(Collectors.toList());
        tableService.recreateTable(table, columns);
        return "";
    };

    public static final Route deleteTable = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        new DeleteTableQuery().setTable(table).executeUpdate();
        return "";
    };

    public static final Route renameColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);
        SQLColumn columnSchema = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), SQLColumn.class);

        new RenameColumnQuery().setTable(table).setFrom(column).setTo(columnSchema.getName()).executeUpdate();
        return "";
    };

    public static final Route addTable = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        SQLColumn columnSchema = new SQLColumn().setName("id").setDatatype(SQLiteDatatype.Integer).setNullable(false).setPk(true);
        new CreateTableQuery().setTableName(table).setColumns(Collections.singletonList(columnSchema)).executeUpdate();
        return "";
    };
}
