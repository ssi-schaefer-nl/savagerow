package io.aero.v2.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.aero.v2.dto.ColumnSchemaDTO;
import io.aero.v2.query.AddColumnQuery;
import io.aero.v2.query.GetTableSchema;
import io.aero.v2.util.RequestParams;
import io.aero.v2.util.SQLiteDataSource;
import spark.Request;
import spark.Response;
import spark.Route;

public class TableSchemaController {

    private TableSchemaController() {
    }

    public static final Route getSchema = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        return new ObjectMapper().writeValueAsString(new GetTableSchema().setTable(table).execute().getResult());
    };


    public static final Route addColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);

        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        ColumnSchemaDTO columnSchema = objectMapper.readValue(request.body(), ColumnSchemaDTO.class);
        new AddColumnQuery().setTable(table).setColumn(columnSchema).generate().execute();
        return "";
    };

    public static final Route deleteColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);

        SQLiteDataSource.getConnection().prepareStatement(String.format("ALTER TABLE %s DROP %s", table, column)).executeUpdate();
        return "";
    };

    public static final Route renameColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        ColumnSchemaDTO columnSchema = objectMapper.readValue(request.body(), ColumnSchemaDTO.class);

        SQLiteDataSource.getConnection().prepareStatement(String.format("ALTER TABLE %s RENAME COLUMN %s TO %s", table, column, columnSchema.getName())).executeUpdate();
        return "";
    };


}
