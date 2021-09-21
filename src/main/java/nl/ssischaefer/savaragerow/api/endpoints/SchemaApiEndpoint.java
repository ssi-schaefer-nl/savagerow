package nl.ssischaefer.savaragerow.api.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.api.dto.TableSchemaDTO;
import nl.ssischaefer.savaragerow.storage.StorageManagementController;
import nl.ssischaefer.savaragerow.storage.common.model.SQLColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class SchemaApiEndpoint {
    public static final String TABLE_PARAM = "table";
    public static final String COLUMN_PARAM = "column";

    private final StorageManagementController storageManagementController;
    private final Logger logger = LoggerFactory.getLogger("SchemaController");

    public SchemaApiEndpoint(StorageManagementController storageManagementController) {
        this.storageManagementController = storageManagementController;
    }

    public void setup(String prefix) {
        String url = prefix + "/database";

        logger.info("Setting up Schema Controller routes with prefix " + url);

        get(url.concat("/:table/schema"), this::getSchema);
        post(url.concat("/:table"), this::addTable);
        post(url.concat("/:table/column"), this::addColumn);
        put(url.concat("/:table/column/:column"), this::renameColumn);
        delete(url.concat("/:table"), this::deleteTable);
        delete(url.concat("/:table/column/:column"), this::deleteColumn);
        get(url.concat("/tables"), this::getTables); }



    public String getSchema(Request request, Response response) throws SQLException, JsonProcessingException {
        String table = request.params(TABLE_PARAM);
        var tableSchemaDTO = new TableSchemaDTO().setName(table).setColumns(storageManagementController.getColumnsForTable(table));
        return new ObjectMapper().writeValueAsString(tableSchemaDTO);
    }

    public String addColumn(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        var objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        SQLColumn column = objectMapper.readValue(request.body(), SQLColumn.class);

        List<SQLColumn> columns = storageManagementController.getColumnsForTable(table);
        columns.add(column);

        storageManagementController.recreateTable(table, columns);
        return "";
    }

    public String deleteColumn(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        String column = request.params(COLUMN_PARAM);

        List<SQLColumn> columns = storageManagementController.getColumnsForTable(table).stream().filter(c -> !c.getName().equals(column)).collect(Collectors.toList());
        storageManagementController.recreateTable(table, columns);
        return "";
    }

    public String deleteTable(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        storageManagementController.deleteTable(table);
        return "";
    }

    public String renameColumn(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        String column = request.params(COLUMN_PARAM);
        SQLColumn columnSchema = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).readValue(request.body(), SQLColumn.class);

        storageManagementController.renameColumn(table, column, columnSchema.getName());

        return "";
    }

    public String addTable(Request request, Response response) throws Exception {
        String table = request.params(TABLE_PARAM);
        storageManagementController.addTable(table);
        return "";
    }

    public String getTables(Request request, Response response) throws SQLException, JsonProcessingException {
        return new ObjectMapper().writeValueAsString(storageManagementController.getTables());
    }

}
