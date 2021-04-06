package io.aero;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static spark.Spark.*;

public class Controller {
    private static String TABLE_PARAM = "tableName";
    private QueryService queryService;

    public Controller(QueryService queryService) {
        this.queryService = queryService;
        System.out.println("starting server...");
        port(9010);
    }

    public void setup() {
        getAllTables();
        getAllRowsOfTable();
        updateRowOfTable();
    }

    private void updateRowOfTable() {
        post("/api/table/:tableName/update", (req, res) -> {
            String tableName = req.params(TABLE_PARAM);
            try {
                RowUpdateDTO rowUpdate = new ObjectMapper().readValue(req.body(), new TypeReference<List<RowUpdateDTO>>() {}).get(0);
                queryService.updateRow(tableName, rowUpdate);
            } catch (Exception e) {
                System.out.println(e);
                res.status(500);
            }
            return "";
        });
    }

    private void getAllRowsOfTable() {
        get("/api/table/:tableName/all", (req, res) -> {
            String tableName = req.params(TABLE_PARAM);
            String ret = "";

            try {
                ret = new ObjectMapper().writeValueAsString(queryService.findAll(tableName));
            } catch (Exception e) {
                System.out.println(e);
                res.status(500);
            }
            return ret;
        });
    }

    private void getAllTables() {
        get("/api/table/all", (req, res) -> {
            String ret = "";
            try {
                ret = new ObjectMapper().writeValueAsString(queryService.listTables());
            } catch (Exception e) {
                System.out.println(e);
                res.status(500);
            }

            return ret;
        });
    }
}
