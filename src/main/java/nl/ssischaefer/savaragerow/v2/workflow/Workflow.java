package nl.ssischaefer.savaragerow.v2.workflow;

import nl.ssischaefer.savaragerow.v1.workflow.TableCondition;
import nl.ssischaefer.savaragerow.v1.workflow.action.Action;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Workflow {
    private String name;
    private boolean active;
    private List<TableCondition> conditions;
    private List<Action> actions;

    public String getName() {
        return name;
    }

    public Workflow setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Workflow setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<TableCondition> getConditions() {
        return conditions;
    }

    public Workflow setConditions(List<TableCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public Workflow setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    /***
     * Executes the workflow
     * @param data data that triggered the workflow, or null if the workflow wasn't triggered by a change in data
     */
    public void execute(Map<String, String> data) {
        if(conditionsSatisfied(data)) {
            actions.sort(Comparator.comparing(Action::getStep));
            actions.forEach(a -> a.perform(data));
        }
    }


    private boolean conditionsSatisfied(Map<String, String> data) {
        return conditions.stream().allMatch(c -> {
            try {
                return c.isSatisfied(data);
            } catch (SQLException throwables) {
                return false;
            }
        });
    }
}
