package nl.ssischaefer.savaragerow.workflow.model.action;

import nl.ssischaefer.savaragerow.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.workflow.util.StringPlaceholderTransformer;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertAction extends CrudAction {
    private Map<String, String> row;


    @Override
    protected List<Map<String, String>> performAction(Map<String, String> data) throws Exception {
        Map<String, String> result = resolvePlaceholdersInRow(data, this.row);
        new InsertRowQuery().setToTable(table).setRow(result).executeUpdate();
        return Collections.singletonList(result);
    }

    @Override
    protected WorkflowTriggerType getTriggerType() {
        return WorkflowTriggerType.INSERT;
    }


    private Map<String, String> resolvePlaceholdersInRow(Map<String, String> data, Map<String, String> target) {
        Map<String, String> temp = new HashMap<>();
        target.forEach((key, value) -> {
            String t = StringPlaceholderTransformer.transformPlaceholders(value, data);
            temp.put(key, t);
        });
        return temp;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public InsertAction setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }

}
