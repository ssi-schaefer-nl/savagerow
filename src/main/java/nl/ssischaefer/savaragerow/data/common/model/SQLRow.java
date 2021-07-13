package nl.ssischaefer.savaragerow.data.common.model;

import java.util.Map;

public class SQLRow {
    Map<String, String> row;

    public Map<String, String> getRow() {
        return row;
    }

    public SQLRow setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }
}
