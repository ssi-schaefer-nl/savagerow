package nl.ssischaefer.savaragerow.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.data.common.exception.DatabaseException;
import nl.ssischaefer.savaragerow.data.management.ManagementService;
import nl.ssischaefer.savaragerow.data.management.query.GetTablesQuery;
import nl.ssischaefer.savaragerow.api.util.RequestParams;
import nl.ssischaefer.savaragerow.workspace.WorkspaceService;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.sql.SQLException;

import static spark.Spark.*;


public class DatabaseController {
    private final ManagementService managementService;

    public DatabaseController(ManagementService managementService) {
        this.managementService = managementService;
    }

    public void setup(String prefix) {
    }





}
