package nl.ssischaefer.savagerow.service;

import nl.ssischaefer.savagerow.dto.RowDTO;
import nl.ssischaefer.savagerow.dto.RowSetDTO;
import nl.ssischaefer.savagerow.dto.TableSchemaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {
    RowSetDTO findAll(String table) throws Exception;
    TableSchemaDTO getSchema(String table) throws Exception;
    List<String> listTables() throws Exception;

    RowDTO find(String table, int rowId) throws Exception;
}
