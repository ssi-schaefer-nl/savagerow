package io.aero;

import io.aero.dto.RowInsertDTO;
import io.aero.dto.RowUpdateDTO;

import java.util.List;
import java.util.Map;

public interface QueryService {
    List<Map<String, String>> findAll(String table) throws Exception;
    List<String> listTables() throws Exception;
    void updateRow(String table, RowUpdateDTO update) throws Exception;
    void addRow(String table, RowInsertDTO newRow) throws Exception;
    void switchDatabase(String database);
}
