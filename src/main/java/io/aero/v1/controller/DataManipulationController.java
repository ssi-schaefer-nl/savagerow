package io.aero.v1.controller;

import io.aero.v1.dto.RowDTO;
import io.aero.v1.dto.RowUpdateDTO;
import io.aero.v1.service.DataManipulationService;
import io.aero.v1.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataManipulationController {
    @Autowired
    DataManipulationService dataManipulationService;
    @Autowired
    WorkspaceService workspaceService;


    @PostMapping(value = "/api/manipulate/{database}/{table}/delete", consumes = "application/json")
    public ResponseEntity<?>  tableDeleteRow(@RequestBody RowDTO row, @PathVariable String database, @PathVariable String table) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            dataManipulationService.deleteRow(table, row);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/manipulate/{database}/{table}/add", consumes = "application/json")
    public ResponseEntity<?> tableAddRow(@RequestBody RowDTO row, @PathVariable String database, @PathVariable String table) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(dataManipulationService.addRow(table, row), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/manipulate/{database}/{table}/update", consumes = "application/json")
    public ResponseEntity<?> tableUpdateRow(@RequestBody RowUpdateDTO rowUpdate, @PathVariable String database, @PathVariable String table) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            dataManipulationService.updateRow(table, rowUpdate);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
