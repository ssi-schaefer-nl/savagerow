package nl.ssischaefer.savaragerow.v1.controller;

import nl.ssischaefer.savaragerow.v1.exceptions.NoDatabaseConnectionException;
import nl.ssischaefer.savaragerow.v1.service.QueryService;
import nl.ssischaefer.savaragerow.v1.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class DataQueryController {
    @Autowired
    QueryService queryService;
    @Autowired
    WorkspaceService workspaceService;

    @GetMapping(value = "/api/query/{database}/tables", produces = "application/json" )
    public ResponseEntity<?> getTables(@PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.listTables(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/query/{database}/{table}/rows/all", produces = "application/json")
    public ResponseEntity<?> getAll(@PathVariable String table, @PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.findAll(table), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(value = "/api/query/{database}/{table}/schema", produces = "application/json")
    public ResponseEntity<?> getSchema(@PathVariable String table, @PathVariable String database) {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            return new ResponseEntity<>(queryService.getSchema(table), HttpStatus.OK);
        } catch (SQLException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NoDatabaseConnectionException e) {
            System.out.println("NoDatabaseConnectionException");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);

        }
    }

}
