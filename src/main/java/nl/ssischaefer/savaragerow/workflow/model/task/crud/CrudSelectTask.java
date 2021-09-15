package nl.ssischaefer.savaragerow.workflow.model.task.crud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudSelectTask extends AbstractCrudWorkflowTask {
    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        Map<String, String> row = repository.get(table, getRowSelectionCriteria(input)).get(0);
        if(row == null) return new HashMap<>();
        return row;
    }

    @Override
    public Map<String, String> getOutput() {
        if(table == null) return new HashMap<>();
        var output = new HashMap<String, String>();
        List<String> columns = repository.getSchema(table);
        columns.forEach(c -> output.put(c, String.format("${%s}", c)));
        return output;
    }
}
