package nl.ssischaefer.savaragerow.data;

import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.common.model.UpdateInstruction;

import java.util.List;
import java.util.Map;

public interface DynamicRepository {
    List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria);
    List<Map<String, String>> getAll(String table);
    List<String> getSchema(String table);
    int delete(String table, List<RowSelectionCriterion> rowSelectionCriteria);
    Long insert(String table, Map<String, String> row);
    int update(String table, List<RowSelectionCriterion> rowSelectionCriteria, List<UpdateInstruction> updateInstructions);
}
