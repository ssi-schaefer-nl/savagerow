package nl.ssischaefer.savaragerow.workflow.trigger;

import nl.ssischaefer.savaragerow.workflow.StorageAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableEventTrigger extends AbstractTrigger {
    private String table;
    private String event;
    private String workflow;
    private final StorageAdapter storageAdapter;

    public TableEventTrigger(StorageAdapter storageAdapter) {
        this.storageAdapter = storageAdapter;
    }


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

    public String getWorkflow() {
        return workflow;
    }

    public void setWorkflow(String workflow) {
        this.workflow = workflow;
    }

    @Override
    public Map<String, String> getOutput() {
        var output = new HashMap<String, String>();
        if (table == null) return output;

        List<String> columns = storageAdapter.getTableColumns(table);
        if(event.equalsIgnoreCase("update")) {
            columns.forEach(c -> output.put(String.format("old.%s", c), String.format("${old.%s}", c)));
            columns.forEach(c -> output.put(String.format("new.%s", c), String.format("${new.%s}", c)));
        } else {
            columns.forEach(c -> output.put(String.format("%s", c), String.format("${%s}", c)));
        }
        return output;
    }

    public boolean isTriggered(String table, String event) {
        return this.table.equalsIgnoreCase(table) && this.event.equalsIgnoreCase(event);
    }
}
