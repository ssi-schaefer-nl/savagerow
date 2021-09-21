package nl.ssischaefer.savaragerow.shared.schema;

import java.util.List;
import java.util.Map;

public class CrudWorkflowTaskSchema extends AbstractWorkflowTaskSchema {
    private String subtype;
    private String table;
    private Map<String, String> rowTemplate;
    private List<RowSelectionCriterionSchema> rowSelectionCriteria;
    private List<UpdateInstructionSchema> updateInstructions;



    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Map<String, String> getRowTemplate() {
        return rowTemplate;
    }

    public void setRowTemplate(Map<String, String> rowTemplate) {
        this.rowTemplate = rowTemplate;
    }

    public List<RowSelectionCriterionSchema> getRowSelectionCriteria() {
        return rowSelectionCriteria;
    }

    public void setRowSelectionCriteria(List<RowSelectionCriterionSchema> rowSelectionCriteria) {
        this.rowSelectionCriteria = rowSelectionCriteria;
    }

    public List<UpdateInstructionSchema> getUpdateInstructions() {
        return updateInstructions;
    }

    public void setUpdateInstructions(List<UpdateInstructionSchema> updateInstructions) {
        this.updateInstructions = updateInstructions;
    }

}
