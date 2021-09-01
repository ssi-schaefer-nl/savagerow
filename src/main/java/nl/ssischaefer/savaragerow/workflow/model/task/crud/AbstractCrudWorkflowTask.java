package nl.ssischaefer.savaragerow.workflow.model.task.crud;


import nl.ssischaefer.savaragerow.common.event.TableEventProducer;
import nl.ssischaefer.savaragerow.data.operations.DynamicRepository;
import nl.ssischaefer.savaragerow.workflow.model.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.commons.text.StringSubstitutor;;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractCrudWorkflowTask extends AbstractWorkflowTask {
    private List<RowSelectionCriterion> rowSelectionCriteriaTemplate;
    protected String table;
    protected DynamicRepository repository;
    protected TableEventProducer eventProducer;


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

    public void setRepository(DynamicRepository dynamicRepository) {
        this.repository = dynamicRepository;
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
        var sub = new StringSubstitutor(input, "{", "}");
        return rowSelectionCriteriaTemplate.stream()
                .map(t -> new RowSelectionCriterion(t.getColumn(), t.getComparator(), sub.replace(t.getValue())))
                .collect(Collectors.toList());
    }

}
