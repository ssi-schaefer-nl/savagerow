package nl.ssischaefer.savaragerow.workflow.model.task.crud;

import java.util.HashMap;
import java.util.Map;

public class CrudSelectTask extends AbstractCrudWorkflowTask {
    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        Map<String, String> row = repository.get(table, getRowSelectionCriteria(input)).get(0);
        if(row == null) return new HashMap<>();
        return row;
    }
}
