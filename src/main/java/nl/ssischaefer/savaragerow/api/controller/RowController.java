package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.data.common.model.SQLRow;
import nl.ssischaefer.savaragerow.data.operations.OperationsService;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import org.apache.commons.lang3.math.NumberUtils;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class RowController {
    private final OperationsService operations;

    public RowController(OperationsService operations) {
        this.operations = operations;
    }

    public void setup(String prefix) {
        get(prefix + "/database/:table/rows", this::getRows);
        post(prefix + "/database/:table/rows", this::addRows);
        put(prefix + "/database/:table/rows/:row", this::updateRow);
        delete(prefix + "/database/:table/rows/:row", this::deleteRow);

    }

    public String getRows(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        String row = request.queryParamOrDefault(RequestParams.Query.Row, "");

        List<Map<String, String>> res;

        if (!row.isEmpty() && NumberUtils.isParsable(row))
            res = operations.get(table, Long.parseLong(row));
        else
            res = operations.get(table);

        return new ObjectMapper().writeValueAsString(res);
    }


    public String addRows(Request request, Response response) throws Exception {
            String table = request.params(RequestParams.Parameter.Table);
            SQLRow row = new ObjectMapper().readValue(request.body(), SQLRow.class);

            return new ObjectMapper().writeValueAsString(operations.insert(table, row.getRow()));

    }


    public String updateRow(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        SQLRow row = new ObjectMapper().readValue(request.body(), SQLRow.class);
        operations.update(table, row.getRow(), Long.valueOf(rowid));
        return "";
    }

    public String deleteRow(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        String rowid = request.params(RequestParams.Parameter.Row);

        if (!rowid.isEmpty() && NumberUtils.isParsable(rowid)) {
            long row = Long.parseLong(rowid);
            operations.delete(table, row);
        }
        return "";
    }
}
