package nl.ssischaefer.savaragerow.workflow.task.crud;


import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import org.apache.commons.text.StringSubstitutor;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CrudUpdateTask extends AbstractCrudWorkflowTask {

    private List<UpdateInstruction> updateInstructionTemplates;

    public List<UpdateInstruction> getUpdateInstructionTemplates() {
        return updateInstructionTemplates;
    }

    public CrudUpdateTask setUpdateInstructionTemplates(List<UpdateInstruction> updateInstructionTemplates) {
        this.updateInstructionTemplates = updateInstructionTemplates;
        return this;
    }

    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        Map<String, String> result = new HashMap<>();
        List<UpdateInstruction> updateInstructions = getUpdateInstructions(input);
        List<RowSelectionCriterion> rowSelectionCriteria = getRowSelectionCriteria(input);

        Map<String, String> rowBeforeUpdate = storageAdapter.get(table, rowSelectionCriteria).get(0);
        if(rowBeforeUpdate != null) {
            String rowid = rowBeforeUpdate.get("rowid");
            List<RowSelectionCriterion> rowIdSelectionCriterion = Collections.singletonList(RowSelectionCriterion.getRowIdCriterion(Long.valueOf(rowid)));

            storageAdapter.update(table, rowIdSelectionCriterion, updateInstructions);
            Map<String, String> rowAfterupdate = storageAdapter.get(table, rowIdSelectionCriterion).get(0);

            rowBeforeUpdate.forEach((key, value) -> result.put("old." + key, value));
            rowAfterupdate.forEach((key, value) -> result.put("new." + key, value));
        }

        return result;
    }

    private List<UpdateInstruction> getUpdateInstructions(Map<String, String> input) {
        var sub = new StringSubstitutor(input, "${", "}");
        return updateInstructionTemplates.stream()
                .map(t -> new UpdateInstruction(t.getField(), t.getOperation(), sub.replace(t.getValue())))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, String> getOutputParameters() {
        if(table == null) return new HashMap<>();
        var output = new HashMap<String, String>();
        List<String> columns = storageAdapter.getTableColumns(table);
        columns.forEach(c -> output.put(String.format("old.%s", c), String.format("${old.%s}", c)));
        columns.forEach(c -> output.put(String.format("new.%s", c), String.format("${new.%s}", c)));
        return output;
    }
}
