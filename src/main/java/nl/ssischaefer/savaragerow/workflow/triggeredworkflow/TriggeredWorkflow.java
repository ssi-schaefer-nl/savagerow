package nl.ssischaefer.savaragerow.workflow.triggeredworkflow;

import nl.ssischaefer.savaragerow.workflow.AbstractWorkflow;
import nl.ssischaefer.savaragerow.workflow.action.Action;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TriggeredWorkflow extends AbstractWorkflow {
    private String table;
    private WorkflowType type;
    private List<Action> actions;

    public WorkflowType getType() {
        return type;
    }

    public TriggeredWorkflow setType(WorkflowType type) {
        this.type = type;
        return this;
    }

    public String getTable() {
        return table;
    }

    public TriggeredWorkflow setTable(String table) {
        this.table = table;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public TriggeredWorkflow setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }


    public void execute(Map<String, String> data) {
        if(conditionsSatisfied(data)) {
            actions.sort(Comparator.comparing(Action::getStep));
            actions.forEach(a -> a.perform(data));
        }
    }
}
