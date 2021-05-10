package io.aero.service;

import io.aero.dto.AddColumnDTO;
import org.springframework.stereotype.Service;

@Service
public interface DataDefinitionService {
    void deleteColumn(String table, String column) throws Exception;
    void addColumn(String table, AddColumnDTO column) throws Exception;

    void renameColumn(String table, String column, String newName) throws Exception;
}
