package nl.ssischaefer.savaragerow.v1.service;

import nl.ssischaefer.savaragerow.v1.dto.RowDTO;
import nl.ssischaefer.savaragerow.v1.dto.RowUpdateDTO;
import org.springframework.stereotype.Service;

@Service
public interface DataManipulationService {
    public void updateRow(String table, RowUpdateDTO update) throws Exception;
    public RowDTO addRow(String table, RowDTO newRow) throws Exception;
    public void deleteRow(String tableName, RowDTO row) throws Exception;
}
