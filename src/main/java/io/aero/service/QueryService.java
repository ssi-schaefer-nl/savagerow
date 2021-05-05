package io.aero.service;

import io.aero.dto.*;
import io.aero.exceptions.NoDatabaseConnectionException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public interface QueryService {
    RowSetDTO findAll(String table) throws Exception;
    TableSchemaDTO getSchema(String table) throws Exception;
    List<String> listTables() throws Exception;
    void updateRow(String table, RowUpdateDTO update) throws Exception;
    RowDTO addRow(String table, RowDTO newRow) throws Exception;
    void deleteRow(String tableName, RowDTO row) throws Exception;
    void deleteColumn(String table, String column) throws Exception;
    void addColumn(String table, AddColumnDTO column) throws Exception;
}
