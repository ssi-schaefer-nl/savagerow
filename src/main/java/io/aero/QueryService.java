package io.aero;

import java.util.List;
import java.util.Map;

public interface QueryService {
    List<Map<String, String>> findAll(String table) throws Exception;
    List<String> listTables() throws Exception;
    void updateRow(String table, RowUpdateDTO update) throws Exception;
}
