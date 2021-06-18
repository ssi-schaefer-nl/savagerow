package nl.ssischaefer.savaragerow.v1.service;

import nl.ssischaefer.savaragerow.v1.dto.RowDTO;
import nl.ssischaefer.savaragerow.v1.dto.RowSetDTO;
import nl.ssischaefer.savaragerow.v1.dto.TableSchemaDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface QueryService {
    RowSetDTO findAll(String table) throws Exception;
    TableSchemaDTO getSchema(String table) throws Exception;
    List<String> listTables() throws Exception;

    RowDTO find(String table, int rowId) throws Exception;
}
