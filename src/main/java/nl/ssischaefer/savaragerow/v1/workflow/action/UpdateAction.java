package nl.ssischaefer.savaragerow.v1.workflow.action;

import nl.ssischaefer.savaragerow.v1.query.dml.UpdateRowQuery;
import nl.ssischaefer.savaragerow.v1.query.dql.GetRowQuery;
import nl.ssischaefer.savaragerow.v1.workflow.FieldUpdate;
import nl.ssischaefer.savaragerow.v1.workflow.RowCriterion;
import nl.ssischaefer.savaragerow.v1.util.PlaceholderResolver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateAction extends CrudAction {
    private List<RowCriterion> rowCriteria;
    private List<FieldUpdate> fieldUpdates;

    @Override
    protected List<Map<String, String>> execute(Map<String, String> data) throws Exception {
        List<RowCriterion> transformedRowCriteria = transformPlaceholdersCriteria(data, rowCriteria);
        List<FieldUpdate> transformedFieldUpdates = transformPlaceholdersFieldupdates(data, fieldUpdates);

        new UpdateRowQuery()
                .setTable(table)
                .setCriteria(transformedRowCriteria)
                .setFieldUpdates(transformedFieldUpdates)
                .execute();

        return new GetRowQuery().setTable(table).setCriteria(transformedRowCriteria).execute().getResult();
    }

    private List<FieldUpdate> transformPlaceholdersFieldupdates(Map<String, String> data, List<FieldUpdate> target) {
        List<FieldUpdate> temp = new ArrayList<>();
        for (FieldUpdate fieldUpdate : target) {
            String t = PlaceholderResolver.resolve(fieldUpdate.getValue(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new FieldUpdate().setColumn(fieldUpdate.getColumn()).setAction(fieldUpdate.getAction()).setValue(t));

        }
        return temp;
    }

    private List<RowCriterion> transformPlaceholdersCriteria(Map<String, String> data, List<RowCriterion> target) {
        List<RowCriterion> temp = new ArrayList<>();
        for (RowCriterion criteria : target) {
            String t = PlaceholderResolver.resolve(criteria.getRequired(), data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.add(new RowCriterion().setColumn(criteria.getColumn()).setComparator(criteria.getComparator()).setRequired(t));

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


    public List<RowCriterion> getRowCriteria() {
        return rowCriteria;
    }

    public UpdateAction setRowCriteria(List<RowCriterion> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }


}
