package nl.ssischaefer.savaragerow.v2.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.savaragerow.query.RowService;
import nl.ssischaefer.savaragerow.v2.api.dto.RowDTO;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dql.FindRowQuery;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.RequestParams;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.WorkflowCache;
import nl.ssischaefer.savaragerow.v2.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue.WorkflowQueue;
import org.apache.commons.lang3.math.NumberUtils;
import spark.Request;
import spark.Response;
import spark.Route;

public class RowController {
    private static final RowService rowService = new RowService(new WorkflowService(WorkflowCache.get(), WorkflowQueue.getQueue()));

    public static final Route getRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String row = request.queryParamOrDefault(RequestParams.Query.Row, "");

        FindRowQuery sqlStatement = new FindRowQuery().setTable(table);
        if (!row.isEmpty() && NumberUtils.isParsable(row)) sqlStatement.setRowId(Long.parseLong(row));
        return new ObjectMapper().writeValueAsString(sqlStatement.execute().getResult());
    };

    public static final Route addRows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);

        return new ObjectMapper().writeValueAsString(rowService.insert(table, row.getRow()));
    };


    public static final Route updateRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        RowDTO row = new ObjectMapper().readValue(request.body(), RowDTO.class);
        rowService.update(table, row.getRow(), Long.valueOf(rowid));
        return "";
    };

    public static final Route deleteRow = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        if (!rowid.isEmpty() && NumberUtils.isParsable(rowid)) {
            long row = Long.parseLong(rowid);
            rowService.delete(table, row);
        }

        return "";
    };
}
