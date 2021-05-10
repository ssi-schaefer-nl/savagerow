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

    RowDTO find(String table, int rowId) throws Exception;
}
