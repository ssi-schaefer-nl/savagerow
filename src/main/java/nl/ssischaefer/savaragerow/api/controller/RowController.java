package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.dto.RowIDs;
import nl.ssischaefer.savaragerow.data.common.model.SQLRow;
import nl.ssischaefer.savaragerow.data.OperationsService;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class RowController {
    private final OperationsService operations;
    private final Logger logger = LoggerFactory.getLogger("RowController");

    public RowController(OperationsService operations) {
        this.operations = operations;
    }

    public void setup(String prefix) {
        String url = prefix + "/database";

        logger.info("Setting up Row Controller routes with prefix " + url);

        get(url.concat("/:table/rows"), this::getRows);
        post(url.concat("/:table/rows"), this::addRows);
        put(url.concat("/:table/rows/:row"), this::updateRow);
        delete(url.concat("/:table/rows"), this::deleteRows);
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

    public String deleteRows(Request request, Response response) throws Exception {
        String table = request.params(RequestParams.Parameter.Table);
        RowIDs ids = new ObjectMapper().readValue(request.body(), RowIDs.class);

        if (ids != null) {
            ids.getIds().forEach(id -> operations.delete(table, id));
        }
        return "";
    }
}
