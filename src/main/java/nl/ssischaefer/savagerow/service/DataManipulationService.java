package nl.ssischaefer.savagerow.service;

import nl.ssischaefer.savagerow.dto.RowDTO;
import nl.ssischaefer.savagerow.dto.RowUpdateDTO;
import org.springframework.stereotype.Service;

@Service
public interface DataManipulationService {
    public void updateRow(String table, RowUpdateDTO update) throws Exception;
    public RowDTO addRow(String table, RowDTO newRow) throws Exception;
    public void deleteRow(String tableName, RowDTO row) throws Exception;
}
