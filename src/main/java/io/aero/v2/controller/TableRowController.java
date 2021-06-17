package io.aero.v2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.aero.v2.dto.RowDTO;
import io.aero.v2.model.WorkflowType;
import io.aero.v2.query.DeleteRowQuery;
import io.aero.v2.query.GetRowQuery;
import io.aero.v2.query.InsertRowQuery;
import io.aero.v2.query.UpdateRowQuery;
import io.aero.v2.util.RequestParams;
import io.aero.v2.workflowqueue.WorkflowTask;
import io.aero.v2.workflowqueue.WorkflowTaskQueue;
import org.apache.commons.lang3.math.NumberUtils;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.SQLException;
import java.util.Map;

public class TableRowController {

    public static final Route getRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String row = request.queryParamOrDefault(RequestParams.Query.Row, "");

        GetRowQuery sqlStatement = new GetRowQuery().setTable(table);
        if (!row.isEmpty() && NumberUtils.isParsable(row)) sqlStatement.setRowId(Long.parseLong(row));
        return new ObjectMapper().writeValueAsString(sqlStatement.generate().execute().getResult());
    };

    public static final Route addRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);

        Map<String, String> res = new InsertRowQuery().setTable(table).setData(row).generate().execute().getResult().get(0);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(res).setTable(table).setType(WorkflowType.INSERT));
        System.out.println(res);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        return new ObjectMapper().writeValueAsString(res);
    };


    public static final Route updateRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);
        new UpdateRowQuery().setTable(table).setRow(row).setRowId(Long.parseLong(rowid)).generate().execute();

        WorkflowTask task = new WorkflowTask()
                .setData(row.getRow())
                .setTable(table)
                .setType(WorkflowType.UPDATE);

        WorkflowTaskQueue.getQueue().feed(task);
        return "";
    };

    public static final Route deleteRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        if (!rowid.isEmpty() && NumberUtils.isParsable(rowid)) {
            long row = Long.parseLong(rowid);

            Map<String, String> data =  new GetRowQuery().setTable(table).setRowId(row).generate().execute().getResult().get(0);
            new DeleteRowQuery().setTable(table).setRow(row).generate().execute();
            WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(data).setTable(table).setType(WorkflowType.DELETE));
        }

        return "";
    };
}
