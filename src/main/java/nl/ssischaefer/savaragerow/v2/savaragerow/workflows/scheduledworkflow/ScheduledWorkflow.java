package nl.ssischaefer.savaragerow.v2.savaragerow.workflows.scheduledworkflow;

import nl.ssischaefer.savaragerow.v2.savaragerow.workflows.AbstractWorkflow;
import nl.ssischaefer.savaragerow.v2.workflow.action.Action;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ScheduledWorkflow extends AbstractWorkflow {
    private List<Action> actions;
    private Period period;

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

    public Period getPeriod() {
        return period;
    }

    public ScheduledWorkflow setPeriod(Period period) {
        this.period = period;
        return this;
    }
}
