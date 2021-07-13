package nl.ssischaefer.savaragerow.v3.workflow.model.action;

import nl.ssischaefer.savaragerow.v3.workflow.model.RowCriteria;
import nl.ssischaefer.savaragerow.v3.workflow.model.WorkflowTriggerType;
import nl.ssischaefer.savaragerow.v3.data.operations.query.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v3.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.v3.util.StringPlaceholderTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private List<RowCriteria> rowCriteria;

    @Override
    protected List<Map<String, String>> performAction(Map<String, String> data) throws Exception {
        List<RowCriteria> resolvedCriteria = resolvePlaceholdersInCriteria(data);
        List<Map<String, String>> deletedRows = new FindRowQuery().setTable(table).setCriteria(resolvedCriteria).executeQuery().getResult();
        new DeleteRowQuery().setTable(table).setCriteria(resolvedCriteria).executeUpdate();
        return deletedRows;
    }

    @Override
    protected WorkflowTriggerType getTriggerType() {
        return WorkflowTriggerType.DELETE;
    }

    private List<RowCriteria> resolvePlaceholdersInCriteria(Map<String, String> data) {
        List<RowCriteria> temp = new ArrayList<>();
        for (RowCriteria criteria : this.rowCriteria) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

        }
        return temp;
    }


    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public DeleteAction setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }


}
