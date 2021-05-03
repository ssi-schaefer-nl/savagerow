package io.aero.controller;

import io.aero.dto.RowDTO;
import io.aero.dto.RowUpdateDTO;
import io.aero.service.QueryService;
import io.aero.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableController {
    @Autowired
    QueryService queryService;
    @Autowired
    WorkspaceService workspaceService;


    @PostMapping(value = "/api/{database}/table/{table}/delete", consumes = "application/json")
    public void tableDeleteRow(@RequestBody RowDTO row, @PathVariable String database, @PathVariable String table) throws Exception {
        workspaceService.setDatabaseIfNotSet(database);
        queryService.deleteRow(table, row);
    }

    @PostMapping(value = "/api/{database}/table/{table}/add", consumes = "application/json")
    public ResponseEntity<?> tableAddRow(@RequestBody RowDTO row, @PathVariable String database, @PathVariable String table) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.addRow(table, row), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/{database}/table/{table}/update", consumes = "application/json")
    public void tableUpdateRow(@RequestBody RowUpdateDTO rowUpdate, @PathVariable String database, @PathVariable String table) throws Exception {
        workspaceService.setDatabaseIfNotSet(database);
        queryService.updateRow(table, rowUpdate);
    }

    @GetMapping(value = "/api/{database}/table/{table}/all", produces = "application/json")
    public ResponseEntity<?> tableGetAll(@PathVariable String table, @PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.findAll(table), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/{database}/table/{table}/schema", produces = "application/json")
    public ResponseEntity<?> tableGetSchema(@PathVariable String table, @PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.getSchema(table), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
