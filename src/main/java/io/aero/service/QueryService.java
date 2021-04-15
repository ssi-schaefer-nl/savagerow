package io.aero.service;

import io.aero.dto.RowDTO;
import io.aero.dto.RowUpdateDTO;
import io.aero.dto.TableDataDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {
    TableDataDTO findAll(String table) throws Exception;
    List<String> listTables() throws Exception;
    void updateRow(String table, RowUpdateDTO update) throws Exception;
    RowDTO addRow(String table, RowDTO newRow) throws Exception;
    void deleteRow(String tableName, RowDTO row) throws Exception;
}
