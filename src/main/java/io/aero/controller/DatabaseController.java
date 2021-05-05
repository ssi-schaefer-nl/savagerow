package io.aero.controller;

import io.aero.service.QueryService;
import io.aero.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DatabaseController {
    @Autowired
    WorkspaceService workspaceService;

    @Autowired
    QueryService queryService;

    @GetMapping(value = "/api/{database}/tables/all", produces = "application/json" )
    public ResponseEntity<?> databaseGetTables(@PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);

            return new ResponseEntity<>(queryService.listTables(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/database/current", produces = "application/json")
    public String tableAddRow() {
        return workspaceService.getCurrentDatabase();
    }

    @GetMapping(value = "/api/database/all", produces = "application/json")
    public  List<String> datasesGetAll() {
        return workspaceService.listDatabases();
    }

    @PostMapping(value = "/api/database/set/{database}", consumes = "application/json")
    public void tableUpdateRow(@PathVariable String database) throws Exception {
        if(workspaceService.databaseExists(database)) {
            workspaceService.setDatabase(database);
        }
    }

}
