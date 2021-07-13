package nl.ssischaefer.savaragerow.v3.workflow.model;

import nl.ssischaefer.savaragerow.v3.workflow.model.action.Action;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Workflow {
    private String identifier;
    private String table;
    private String name;
    private boolean active;
    private WorkflowTriggerType type;
    private List<Action> actions;
    private List<TableCondition> conditions;

    public WorkflowTriggerType getType() {
        return type;
    }

    public Workflow setTriggerType(WorkflowTriggerType type) {
        this.type = type;
        return this;
    }

    public String getTable() {
        return table;
    }

    public Workflow setTable(String table) {
        this.table = table;
        return this;
    }

    public String getName() {
        return name;
    }

    public Workflow setName(String name) {
        this.name = name;
        return this;
    }

    public List<Action> getActions() {
        return actions;
    }

    public Workflow setActions(List<Action> actions) {
        this.actions = actions;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Workflow setActive(boolean active) {
        this.active = active;
        return this;
    }

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
            } catch (Exception throwables) {
                return false;
            }
        });
    }


    public List<TableCondition> getConditions() {
        return conditions;
    }

    public Workflow setConditions(List<TableCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public Workflow setIdentifier(String identifier) {
        this.identifier = identifier;
        return this;
    }


    public void setType(WorkflowTriggerType type) {
        this.type = type;
    }
}
