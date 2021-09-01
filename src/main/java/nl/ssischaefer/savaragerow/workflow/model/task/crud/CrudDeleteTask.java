package nl.ssischaefer.savaragerow.workflow.model.task.crud;

import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudDeleteTask extends AbstractCrudWorkflowTask {
    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        List<RowSelectionCriterion> rowSelectionCriteria = getRowSelectionCriteria(input);
        List<Map<String, String>> rows = repository.get(table, rowSelectionCriteria);

        if(!rows.isEmpty()) {
            Map<String, String> row = rows.get(0);
            String rowid = row.get("rowid");
            repository.delete(table, Collections.singletonList(RowSelectionCriterion.getRowIdCriterion(Long.valueOf(rowid))));
            return row;
        }
        return new HashMap<>();
    }
}
