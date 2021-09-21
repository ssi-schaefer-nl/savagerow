package nl.ssischaefer.savaragerow.workflow.task.crud;

import nl.ssischaefer.savaragerow.workflow.exception.WorkflowTaskExecutionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudSelectTask extends AbstractCrudWorkflowTask {
    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        List<Map<String, String>> row = storageAdapter.get(table, getRowSelectionCriteria(input));;
        if (row.isEmpty() || row.get(0) == null) return new HashMap<>();
        return row.get(0);
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
