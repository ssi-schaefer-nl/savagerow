package nl.ssischaefer.savaragerow.v2.workflow.action;

import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.DeleteRowQuery;
import nl.ssischaefer.savaragerow.v2.data.operations.query.dql.FindRowQuery;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.PlaceholderResolver;
import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.RowCriterion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private List<RowCriterion> rowCriteria;

    @Override
    protected List<Map<String, String>> execute(Map<String, String> data) throws Exception {
        List<RowCriterion> transformedRowCriteria = transformPlaceholders(data, rowCriteria);

        List<Map<String, String>> deletedRows = new FindRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute().getResult();
        new DeleteRowQuery().setTable(table).setCriteria(transformedRowCriteria).executeUpdate();
        return deletedRows;
    }

    private List<RowCriterion> transformPlaceholders(Map<String, String> data, List<RowCriterion> target) {
        List<RowCriterion> temp = new ArrayList<>();
        for (RowCriterion criteria : target) {
            String t = PlaceholderResolver.resolve(criteria.getRequired(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new RowCriterion().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

        }
        return temp;
    }


    public List<RowCriterion> getRowCriteria() {
        return rowCriteria;
    }

    public DeleteAction setRowCriteria(List<RowCriterion> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }


}
