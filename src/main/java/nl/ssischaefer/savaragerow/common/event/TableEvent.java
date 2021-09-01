package nl.ssischaefer.savaragerow.common.event;

import java.util.Map;

public class TableEvent {
    private Map<String, String> data;
    private String table;
    private String type;

    public TableEvent(String table, Map<String, String> data, String type) {
        this.data = data;
        this.table = table;
        this.type = type;
    }


    public Map<String, String> getData() {
        return data;
    }


    public String getTable() {
        return table;
    }

    public String getType() {
        return type;
    }

}
