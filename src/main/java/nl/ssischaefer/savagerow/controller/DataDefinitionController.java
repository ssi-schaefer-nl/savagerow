package nl.ssischaefer.savagerow.controller;


import nl.ssischaefer.savagerow.dto.AddColumnDTO;
import nl.ssischaefer.savagerow.service.DataDefinitionService;
import nl.ssischaefer.savagerow.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DataDefinitionController {
    @Autowired
    DataDefinitionService dataDefinitionService;
    @Autowired
    WorkspaceService workspaceService;

    @PostMapping(value = "/api/definition/{database}/{table}/column/add", consumes = "application/json")
    public ResponseEntity<?> tableAddColumn(@RequestBody AddColumnDTO column, @PathVariable String database, @PathVariable String table) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            dataDefinitionService.addColumn(table, column);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(value = "/api/definition/{database}/{table}/{column}/drop")
    public ResponseEntity<?> tableDropColumn(@PathVariable String database, @PathVariable String table, @PathVariable String column) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            dataDefinitionService.deleteColumn(table, column);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(value = "/api/definition/{database}/{table}/{column}/rename/{newName}")
    public ResponseEntity<?> tableRenameColumn(@PathVariable String database, @PathVariable String table, @PathVariable String column, @PathVariable String newName) throws Exception {
        try {
            workspaceService.setDatabaseIfNotSet(database);
            dataDefinitionService.renameColumn(table, column, newName);
            return new ResponseEntity<>("", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
