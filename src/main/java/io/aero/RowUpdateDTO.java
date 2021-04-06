package io.aero;

import java.util.Map;

public class RowUpdateDTO {
    Map<String, String> oldRow;
    Map<String, String> newRow;

    public Map<String, String> getOldRow() {
        return oldRow;
    }

    public RowUpdateDTO setOldRow(Map<String, String> oldRow) {
        this.oldRow = oldRow;
        return this;
    }

    public Map<String, String> getNewRow() {
        return newRow;
    }

    public RowUpdateDTO setNewRow(Map<String, String> newRow) {
        this.newRow = newRow;
        return this;
    }
}
