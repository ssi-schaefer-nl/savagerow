package io.aero.v2.query;

import io.aero.v2.model.action.FieldUpdate;
import io.aero.v2.model.action.RowCriteria;
import io.aero.v2.util.OperatorTransformer;
import io.aero.v2.util.SQLiteDataSource;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class UpdateRowsByCriteriaAndTransformActions {
    private String table;
    private List<RowCriteria> criteria;
    private String sql;
    private PreparedStatement preparedStatement;
    private List<PreparedStatement> preparedStatements;
    private List<UpdateRowQuery> generatedRowUpdates;
    private List<FieldUpdate> fieldUpdates;

    public UpdateRowsByCriteriaAndTransformActions setTable(String table) {
        this.table = table;
        return this;
    }

    public UpdateRowsByCriteriaAndTransformActions setCriteria(List<RowCriteria> criteria) {
        this.criteria = criteria;
        return this;
    }

    public UpdateRowsByCriteriaAndTransformActions generate() throws SQLException {
        String setClause = generateSetClause();
        String whereClause = generateWhereClause();
        sql = String.format("UPDATE %s SET %s WHERE %s", this.table, setClause, whereClause);
        this.preparedStatement = SQLiteDataSource.getConnection().prepareStatement(sql);

        int nextParamIndex = 0;
        for (FieldUpdate update : fieldUpdates) {
            nextParamIndex++;
            if(update.getAction().equals("set"))
                preparedStatement.setString(nextParamIndex, update.getValue());
            else
                preparedStatement.setLong(nextParamIndex, Long.parseLong(update.getValue()));
        }

        for (RowCriteria c : criteria) {
            nextParamIndex++;
            preparedStatement.setString(nextParamIndex, c.getRequired());
        }
        return this;
    }

    private String generateWhereClause() {
        return criteria.stream()
                .map(c -> String.format("%s %s ?", c.getColumn(), OperatorTransformer.convertToSql(c.getOperator())))
                .collect(Collectors.joining(" AND "));
    }

    private String generateSetClause() {
        return fieldUpdates
                .stream()
                .map(f -> String.format("%s = %s", f.getColumn(), generateSetAction(f)))
                .collect(Collectors.joining(", "));
    }


    private String generateSetAction(FieldUpdate fieldUpdate) {
        switch (fieldUpdate.getAction()) {
            case "subtract":
                return String.format("%s - ?", fieldUpdate.getColumn());
            case "add":
                return String.format("%s + ?", fieldUpdate.getColumn());
            case "multiply":
                return String.format("%s * ?", fieldUpdate.getColumn());
            default:
                return "?";
        }
    }


    public void execute() throws SQLException {
        if (generatedRowUpdates != null) {
            for (UpdateRowQuery query : generatedRowUpdates) query.execute();
        } else {
            preparedStatement.executeUpdate();
        }
    }

    public UpdateRowsByCriteriaAndTransformActions setFieldUpdates(List<FieldUpdate> fieldUpdates) {
        this.fieldUpdates = fieldUpdates;
        return this;
    }
}
