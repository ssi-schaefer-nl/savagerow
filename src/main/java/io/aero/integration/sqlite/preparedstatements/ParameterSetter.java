package io.aero.integration.sqlite.preparedstatements;

import io.aero.integration.sqlite.metadata.ColumnMetaData;
import io.aero.integration.sqlite.utils.SQLiteDatatypeConverter;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

public class ParameterSetter {
    private int index = 1;
    private final PreparedStatement statement;

    public ParameterSetter(PreparedStatement statement) {
        this.statement = statement;
    }

    public void setParameters(List<String> sortedColumns, Map<String, String> colValMap, List<ColumnMetaData> columnMetaData) throws Exception {
        for (String col : sortedColumns) {
            int type = columnMetaData.stream().filter(c -> c.getName().equals(col)).findFirst().map(ColumnMetaData::getDatatype).orElse(0);
            setParameterWithAppropriateType(index, colValMap.get(col), type);
            index++;
        }
    }

    private void setParameterWithAppropriateType(int paramIndex, String colVal, int type) throws Exception {
        if(colVal == null || colVal.isEmpty()) {
            statement.setNull(paramIndex, type);
            return;
        }
        Object data = SQLiteDatatypeConverter.convertStringToType(colVal, type);
        statement.setObject(paramIndex, data, type);
    }


}
