package nl.ssischaefer.savaragerow.workflow.scheduledworkflow;

import nl.ssischaefer.savaragerow.workflow.AbstractWorkflow;
import nl.ssischaefer.savaragerow.workflow.action.Action;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ScheduledWorkflow extends AbstractWorkflow {
    private List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public ScheduledWorkflow setActions(List<Action> actions) {
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
