package nl.ssischaefer.savaragerow.api.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.dto.RowIDs;
import nl.ssischaefer.savaragerow.storage.common.model.SQLRow;
import nl.ssischaefer.savaragerow.storage.StorageOperationsController;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class RowApiEndpoint {
    public static final String TABLE_PARAM = "table";
    public static final String ROW_PARAM = "row";

    private final StorageOperationsController operationsController;
    private final Logger logger = LoggerFactory.getLogger("RowController");

    public RowApiEndpoint(StorageOperationsController operationsController) {
        this.operationsController = operationsController;
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
        String table = request.params(TABLE_PARAM);
        String row = request.queryParamOrDefault(ROW_PARAM, "");

        List<Map<String, String>> res;

        if (!row.isEmpty() && NumberUtils.isParsable(row))
            res = operationsController.get(table, Long.parseLong(row));
        else
            res = operationsController.get(table);

        return new ObjectMapper().writeValueAsString(res);
    }


    public String addRows(Request request, Response response) throws Exception {
            String table = request.params(TABLE_PARAM);
            SQLRow row = new ObjectMapper().readValue(request.body(), SQLRow.class);

            return new ObjectMapper().writeValueAsString(operationsController.insert(table, row.getRow()));

    }


    public String updateRow(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        String rowid = request.params(ROW_PARAM);

        SQLRow row = new ObjectMapper().readValue(request.body(), SQLRow.class);
        operationsController.update(table, row.getRow(), Long.valueOf(rowid));
        return "";
    }

    public String deleteRows(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        RowIDs ids = new ObjectMapper().readValue(request.body(), RowIDs.class);

        if (ids != null) {
            ids.getIds().forEach(id -> operationsController.delete(table, id));
        }
        return "";
    }
}
