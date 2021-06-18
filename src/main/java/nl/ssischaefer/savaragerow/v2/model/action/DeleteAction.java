package nl.ssischaefer.savaragerow.v2.model.action;

import nl.ssischaefer.savaragerow.v2.model.RowCriteria;
import nl.ssischaefer.savaragerow.v2.model.WorkflowType;
import nl.ssischaefer.savaragerow.v2.query.DeleteRowByCriteriaQuery;
import nl.ssischaefer.savaragerow.v2.util.StringPlaceholderTransformer;
import nl.ssischaefer.savaragerow.v2.workflowqueue.WorkflowTask;
import nl.ssischaefer.savaragerow.v2.workflowqueue.WorkflowTaskQueue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeleteAction extends CrudAction {
    private String table;
    private List<RowCriteria> rowCriteria;

    @Override
    public void perform(Map<String, String> data) {
        List<RowCriteria> transformedRowCriteria = transformPlaceholders(data, rowCriteria);

        try {
            List<Map<String, String>> deletedRows = new DeleteRowByCriteriaQuery()
                    .setTable(table)
                    .setCriteria(transformedRowCriteria)
                    .setStoreDeletedRows(triggerWorkflows)
                    .generate()
                    .execute()
                    .getDeletedRows();

            if(triggerWorkflows) {
                deletedRows.forEach(r -> WorkflowTaskQueue.getQueue().feed(new WorkflowTask().setData(r).setTable(table).setType(WorkflowType.DELETE)));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private List<RowCriteria> transformPlaceholders(Map<String, String> data, List<RowCriteria> target) {
        List<RowCriteria> temp = new ArrayList<>();
        for(RowCriteria criteria : target) {
            String t = StringPlaceholderTransformer.transformPlaceholders(criteria.getRequired(), data);
            temp.add(new RowCriteria().setColumn(criteria.getColumn()).setOperator(criteria.getOperator()).setRequired(t));

        }
        return temp;
    }


    public String getTable() {
        return table;
    }

    public DeleteAction setTable(String table) {
        this.table = table;
        return this;
    }

    public List<RowCriteria> getRowCriteria() {
        return rowCriteria;
    }

    public DeleteAction setRowCriteria(List<RowCriteria> rowCriteria) {
        this.rowCriteria = rowCriteria;
        return this;
    }


}
