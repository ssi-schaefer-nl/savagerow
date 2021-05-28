package io.aero.v1.service;

import io.aero.v1.dto.*;
import io.aero.v1.exceptions.NoDatabaseConnectionException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {
    RowSetDTO findAll(String table) throws Exception;
    TableSchemaDTO getSchema(String table) throws Exception;
    List<String> listTables() throws Exception;

    RowDTO find(String table, int rowId) throws Exception;
}
