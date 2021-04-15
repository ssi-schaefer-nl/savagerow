package io.aero.controller;

import io.aero.dto.RowDTO;
import io.aero.dto.RowUpdateDTO;
import io.aero.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TableController {
    @Autowired
    QueryService queryService;

    @PostMapping(value = "/api/table/{table}/delete", consumes = "application/json")
    public void tableDeleteRow(@RequestBody RowDTO row, @PathVariable String table) throws Exception {
        queryService.deleteRow(table, row);
    }

    @PostMapping(value = "/api/table/{table}/add", consumes = "application/json")
    public ResponseEntity<?> tableAddRow(@RequestBody RowDTO row, @PathVariable String table) {
        try {
            return new ResponseEntity<>(queryService.addRow(table, row), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/table/{table}/update", consumes = "application/json")
    public void tableUpdateRow(@RequestBody RowUpdateDTO rowUpdate, @PathVariable String table) throws Exception {
        queryService.updateRow(table, rowUpdate);
    }

    @GetMapping(value = "/api/table/{table}/all", produces = "application/json")
    public ResponseEntity<?> tableGetAll(@PathVariable String table) {
        try {
            return new ResponseEntity<>(queryService.findAll(table), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
