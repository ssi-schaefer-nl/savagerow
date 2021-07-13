package nl.ssischaefer.savaragerow.workflow.model.action;

import nl.ssischaefer.savaragerow.data.operations.query.UpdateRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.workflow.model.FieldUpdate;
import nl.ssischaefer.savaragerow.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.util.StringPlaceholderTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateAction extends CrudAction {
    private List<RowCriteria> rowCriteria;
    private List<FieldUpdate> fieldUpdates;

    @Override
    public List<Map<String, String>> performAction(Map<String, String> data) throws Exception {
        List<RowCriteria> criteria = resolvePlaceholdersInCriteria(data);
        List<FieldUpdate> updates = resolvePlaceholdersFieldupdates(data);

        new UpdateRowQuery().setTable(table).setCriteria(criteria).setFieldUpdates(updates).executeUpdate();
        return new FindRowQuery().setTable(table).setCriteria(criteria).executeQuery().getResult();
    }

    @Override
    protected WorkflowTriggerType getTriggerType() {
        return WorkflowTriggerType.UPDATE;
    }


    private List<FieldUpdate> resolvePlaceholdersFieldupdates(Map<String, String> data) {
        List<FieldUpdate> temp = new ArrayList<>();
        for (FieldUpdate fieldUpdate : fieldUpdates) {
            String t = StringPlaceholderTransformer.transformPlaceholders(fieldUpdate.getValue(), data);
            temp.add(new FieldUpdate().setColumn(fieldUpdate.getColumn()).setAction(fieldUpdate.getAction()).setValue(t));

        }
        return temp;
    }

    private List<RowCriteria> resolvePlaceholdersInCriteria(Map<String, String> data) {
        List<RowCriteria> temp = new ArrayList<>();
        for (RowCriteria criteria : rowCriteria) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

        }
        return temp;
    }

    public List<FieldUpdate> getFieldUpdates() {
        return fieldUpdates;
    }

    public UpdateAction setFieldUpdates(List<FieldUpdate> fieldUpdates) {
        this.fieldUpdates = fieldUpdates;
        return this;
    }

    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public UpdateAction setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }

}
