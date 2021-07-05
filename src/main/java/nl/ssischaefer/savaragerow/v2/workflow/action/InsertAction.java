package nl.ssischaefer.savaragerow.v2.workflow.action;

import nl.ssischaefer.savaragerow.v2.data.operations.query.dml.InsertRowQuery;
import nl.ssischaefer.savaragerow.v2.savaragerow.util.PlaceholderResolver;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertAction extends CrudAction {
    private Map<String, String> row;


    @Override
    protected List<Map<String, String>> execute(Map<String, String> data) throws Exception {
        Map<String, String> transformedRow = transformPlaceholders(data, row);
        new InsertRowQuery().setToTable(table).setRow(transformedRow).executeUpdate();
        return Collections.singletonList(transformedRow);
    }


    private Map<String, String> transformPlaceholders(Map<String, String> data, Map<String, String> target) {
        Map<String, String> temp = new HashMap<>();
        target.forEach((key, value) -> {
            String t = PlaceholderResolver.resolve(value, data, PlaceholderResolver.BRACKETS_FORMAT);
            temp.put(key, t);
        });
        return temp;
    }

    public Map<String, String> getRow() {
        return row;
    }

    public InsertAction setRow(Map<String, String> row) {
        this.row = row;
        return this;
    }


}
