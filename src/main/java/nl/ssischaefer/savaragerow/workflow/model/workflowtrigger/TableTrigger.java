package nl.ssischaefer.savaragerow.workflow.model.workflowtrigger;

import nl.ssischaefer.savaragerow.data.DynamicRepository;
import nl.ssischaefer.savaragerow.data.DynamicRepositoryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableTrigger extends AbstractWorkflowTrigger {
    private String table;
    private String event;
    protected DynamicRepository repository;

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

    @Override
    public Map<String, String> getOutput() {
        if (table == null) return new HashMap<>();
        var output = new HashMap<String, String>();
        List<String> columns = repository.getSchema(table);        if(event.equalsIgnoreCase("update")) {
            columns.forEach(c -> output.put(String.format("old.%s", c), String.format("${old.%s}", c)));
            columns.forEach(c -> output.put(String.format("new.%s", c), String.format("${new.%s}", c)));
        } else {
            columns.forEach(c -> output.put(String.format("%s", c), String.format("${%s}", c)));
        }
        return output;
    }

    public TableTrigger setDynamicRepository(DynamicRepository repository) {
        this.repository = repository;
        return this;
    }

    protected String getTriggerCause() { return String.format("table event %s on table %s", event, table); }
}
