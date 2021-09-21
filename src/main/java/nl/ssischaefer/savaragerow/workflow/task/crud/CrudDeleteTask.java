package nl.ssischaefer.savaragerow.workflow.task.crud;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudDeleteTask extends AbstractCrudWorkflowTask {
    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        List<RowSelectionCriterion> rowSelectionCriteria = getRowSelectionCriteria(input);
        List<Map<String, String>> rows = storageAdapter.get(table, rowSelectionCriteria);

        if(!rows.isEmpty()) {
            Map<String, String> row = rows.get(0);
            String rowid = row.get("rowid");
            storageAdapter.delete(table, Collections.singletonList(RowSelectionCriterion.getRowIdCriterion(Long.valueOf(rowid))));
            return row;
        }
        return new HashMap<>();
    }

    @Override
    public Map<String, String> getOutputParameters() {
        if(table == null) return new HashMap<>();
        var output = new HashMap<String, String>();
        List<String> columns = storageAdapter.getTableColumns(table);
        columns.forEach(c -> output.put(c, String.format("${%s}", c)));
        return output;
    }
}