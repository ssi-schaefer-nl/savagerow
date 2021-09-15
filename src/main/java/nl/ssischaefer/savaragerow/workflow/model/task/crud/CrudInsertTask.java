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

    @Override
    public Map<String, String> getOutput() {
        if(table == null) return new HashMap<>();
        var output = new HashMap<String, String>();
        List<String> row = repository.getSchema(table);
        row.forEach(c -> output.put(c, String.format("${%s}", c)));
        return output;
    }

    public Map<String, String> getRow(Map<String, String> input) {
        var sub = new StringSubstitutor(input, "${", "}");
        Map<String, String> columns = new HashMap<>();
        rowTemplate.forEach((key, value) -> columns.put(key, sub.replace(value)));
        return columns;
    }
}
