package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;

import java.util.List;
import java.util.Map;

public interface StorageAdapter {
    List<String> getTableColumns(String table);
    List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria);

    void delete(String table, List<RowSelectionCriterion> rowSelectionCriteria);

    Long insert(String table, Map<String, String> row);

    void update(String table, List<RowSelectionCriterion> rowIdSelectionCriterion, List<UpdateInstruction> updateInstructions);
}
