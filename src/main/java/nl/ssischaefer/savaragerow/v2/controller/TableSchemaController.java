package nl.ssischaefer.savaragerow.v2.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.ssischaefer.savaragerow.v2.dto.ColumnSchemaDTO;
import nl.ssischaefer.savaragerow.v2.query.AddColumnQuery;
import nl.ssischaefer.savaragerow.v2.query.CreateTableQuery;
import nl.ssischaefer.savaragerow.v2.query.GetTableSchema;
import nl.ssischaefer.savaragerow.v2.query.RecreateTableQuery;
import nl.ssischaefer.savaragerow.v2.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDataSource;
import nl.ssischaefer.savaragerow.v2.util.SQLiteDatatype;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        List<ColumnSchemaDTO> columns = new GetTableSchema().setTable(table).execute().getResult().getColumns().stream().filter(c -> !c.getName().equals(column)).collect(Collectors.toList());
        new RecreateTableQuery().setTable(table).setColumns(columns).generate().execute();
        return "";
    };

    public static final Route deleteTable = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);


        String sql = String.format("DROP TABLE %s", table);
        PreparedStatement preparedStatement = SQLiteDataSource.get().prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        return "";
    };

    public static final Route renameColumn = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String column = request.params(RequestParams.Parameter.Column);
        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        ColumnSchemaDTO columnSchema = objectMapper.readValue(request.body(), ColumnSchemaDTO.class);

        SQLiteDataSource.get().prepareStatement(String.format("ALTER TABLE %s RENAME COLUMN %s TO %s", table, column, columnSchema.getName())).executeUpdate();
        return "";
    };

    public static final Route addTable = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        ColumnSchemaDTO columnSchema = new ColumnSchemaDTO().setName("id").setDatatype(SQLiteDatatype.Integer).setNullable(false).setPk(true);
        new CreateTableQuery().setTableName(table).setColumns(Collections.singletonList(columnSchema)).generate().execute();
        return "";
    };
}
