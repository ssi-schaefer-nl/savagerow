package nl.ssischaefer.savaragerow.workflow.model.workflowtrigger;

import java.util.Map;

public class TableTrigger extends AbstractWorkflowTrigger {
    private String table;
    private String event;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

}
