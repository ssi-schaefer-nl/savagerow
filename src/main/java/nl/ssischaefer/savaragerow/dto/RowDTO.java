package nl.ssischaefer.savaragerow.dto;

import java.util.Map;

public class RowDTO {
    Map<String, String> row;

    public Map<String, String> getRow() {
        return row;
    }

    public RowDTO setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }
}
