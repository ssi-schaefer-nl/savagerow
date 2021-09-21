package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;

import java.util.List;
import java.util.Map;

public interface StorageService {
    List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria);
    List<Map<String, String>> getAll(String table);
    List<String> getSchema(String table);
    int delete(String table, List<RowSelectionCriterion> rowSelectionCriteria);
    Long insert(String table, Map<String, String> row);
    int update(String table, List<RowSelectionCriterion> rowSelectionCriteria, List<UpdateInstruction> updateInstructions);
}
