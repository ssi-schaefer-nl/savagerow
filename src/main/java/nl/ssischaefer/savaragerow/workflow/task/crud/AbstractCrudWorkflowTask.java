package nl.ssischaefer.savaragerow.workflow.task.crud;


import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.workflow.StorageAdapter;
import nl.ssischaefer.savaragerow.workflow.TableEventProducer;
import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;
import org.apache.commons.text.StringSubstitutor;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

;

public abstract class AbstractCrudWorkflowTask extends AbstractWorkflowTask {
    private List<RowSelectionCriterion> rowSelectionCriteriaTemplate;
    private Long next;
    protected String table;
    protected StorageAdapter storageAdapter;
    protected TableEventProducer eventProducer;

    @Override
    public Long getNext() {
        return next;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public AbstractCrudWorkflowTask setRowSelectionCriteriaTemplate(List<RowSelectionCriterion> rowSelectionCriteriaTemplate) {
        this.rowSelectionCriteriaTemplate = rowSelectionCriteriaTemplate;
        return this;
    }

    public void setStorageAdapter(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }

    public void setEventProducer(TableEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    /***
     * Maps the input values to placeholders defined in the row selection criteria templates.
     * @param input maps placeholders (key) to values (value)
     * @return List RowSelectionCriterion objects transformed from the templates with resolved placeholders.
     */
    public List<RowSelectionCriterion> getRowSelectionCriteria(Map<String, String> input) {
        var sub = new StringSubstitutor(input, "${", "}");
        return rowSelectionCriteriaTemplate.stream()
                .map(t -> new RowSelectionCriterion(t.getColumn(), t.getComparator(), sub.replace(t.getValue())))
                .collect(Collectors.toList());
    }

    public void setNext(Long next) {
        this.next = next;
    }
}
