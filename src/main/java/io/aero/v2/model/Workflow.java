package io.aero.v2.model;

import io.aero.v2.model.action.Action;

import java.util.List;

public class Workflow {
    private String table;
    private String name;
    private boolean active;
    private WorkflowType type;
    private List<Action> actions;

    public WorkflowType getType() {
        return type;
    }

    public Workflow setType(WorkflowType type) {
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
}
