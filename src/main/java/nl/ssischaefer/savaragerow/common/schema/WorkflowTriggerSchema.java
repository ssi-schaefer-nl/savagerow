package nl.ssischaefer.savaragerow.common.schema;

public class WorkflowTriggerSchema {
    private String triggerType;
    private String table;
    private String tableEvent;
    private Long task;

    public String getTriggerType() {
        return triggerType;
    }

    public void setTriggerType(String triggerType) {
        this.triggerType = triggerType;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getTableEvent() {
        return tableEvent;
    }

    public void setTableEvent(String tableEvent) {
        this.tableEvent = tableEvent;
    }

    public Long getTask() {
        return task;
    }

    public void setTask(Long task) {
        this.task = task;
    }
}
