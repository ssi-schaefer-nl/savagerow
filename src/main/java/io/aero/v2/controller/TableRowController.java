package io.aero.v2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.query.DeleteRowQuery;
import io.aero.v2.query.GetRowQuery;
import io.aero.v2.query.InsertRowQuery;
import io.aero.v2.query.UpdateRowQuery;
import io.aero.v2.util.RequestParams;
import org.apache.commons.lang3.math.NumberUtils;
import spark.Request;
import spark.Response;
import spark.Route;

public class TableRowController {
    private TableRowController() {
    }

    public static final Route getRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String row = request.queryParamOrDefault(RequestParams.Query.Row, "");

        GetRowQuery sqlStatement = new GetRowQuery().setTable(table);
        if (!row.isEmpty() && NumberUtils.isParsable(row)) sqlStatement.setRowId(Integer.valueOf(row));
        return new ObjectMapper().writeValueAsString(sqlStatement.generate().execute().getResult());
    };

    public static final Route addRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);

        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);
        return new ObjectMapper().writeValueAsString(new InsertRowQuery().setTable(table).setData(row).generate().execute().getResult().get(0));
    };


    public static final Route updateRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(Long.parseLong(rowid)).generate().execute();
        return "";
    };

    public static final Route deleteRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        new DeleteRowQuery().setTable(table).setRow(Long.parseLong(rowid)).generate().execute();
        return "";
    };
}
