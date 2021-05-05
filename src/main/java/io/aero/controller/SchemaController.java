package io.aero.controller;


import io.aero.dto.AddColumnDTO;
import io.aero.service.QueryService;
import io.aero.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SchemaController {
    @Autowired
    QueryService queryService;
    @Autowired
    WorkspaceService workspaceService;

    @PostMapping(value = "/api/{database}/{table}/column/add", consumes = "application/json")
    public ResponseEntity<?> tableAddColumn(@RequestBody AddColumnDTO column, @PathVariable String database, @PathVariable String table) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            queryService.addColumn(table, column);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/api/{database}/{table}/{column}/drop")
    public ResponseEntity<?> tableDropColumn(@PathVariable String database, @PathVariable String table, @PathVariable String column) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            queryService.deleteColumn(table, column);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
