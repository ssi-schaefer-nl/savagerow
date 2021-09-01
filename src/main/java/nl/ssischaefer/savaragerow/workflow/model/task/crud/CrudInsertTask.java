package nl.ssischaefer.savaragerow.workflow.model.task.crud;

import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import org.apache.commons.text.StringSubstitutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrudInsertTask extends AbstractCrudWorkflowTask {
    private Map<String, String> rowTemplate;

    public Map<String, String> getRowTemplate() {
        return rowTemplate;
    }

    public CrudInsertTask setRowTemplate(Map<String, String> rowTemplate) {
        this.rowTemplate = rowTemplate;
        return this;
    }

    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        Long rowid = repository.insert(table, getRow(input));
        return repository.get(table, Collections.singletonList(RowSelectionCriterion.getRowIdCriterion(rowid))).get(0);
    }

    public Map<String, String> getRow(Map<String, String> input) {
        var sub = new StringSubstitutor(input, "{", "}");
        Map<String, String> row = new HashMap<>();
        rowTemplate.forEach((key, value) -> row.put(key, sub.replace(value)));
        return row;
    }
}
