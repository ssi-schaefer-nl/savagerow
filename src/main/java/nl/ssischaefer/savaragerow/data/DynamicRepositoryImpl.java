package nl.ssischaefer.savaragerow.data.operations;

import nl.ssischaefer.savaragerow.common.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.common.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.data.operations.query.DeleteRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.FindRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.InsertRowQuery;
import nl.ssischaefer.savaragerow.data.operations.query.UpdateRowQuery;

import java.util.*;

public class DynamicRepository {
    public List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria) {
        try {
            return new FindRowQuery().setTable(table).setCriteria(rowSelectionCriteria).executeQuery().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Map<String, String>> nGet(String table, int n) {
        try {
            return new FindRowQuery().setTable(table).setTop(n).executeQuery().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Map<String, String>> getAll(String table) {
        try {
            return new FindRowQuery().setTable(table).executeQuery().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }


    public int delete(String table, List<RowSelectionCriterion> rowSelectionCriteria) {
        try {
            return new DeleteRowQuery().setTable(table).setCriteria(rowSelectionCriteria).executeUpdate().getAffectedRows();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public Long insert(String table, Map<String, String> row) {
        try {
            return new InsertRowQuery().setToTable(table).setRow(row).executeUpdate().getGeneratedKey();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int update(String table, List<RowSelectionCriterion> rowSelectionCriteria, List<UpdateInstruction> updateInstructions) {
        try {
            return new UpdateRowQuery().setTable(table).setUpdateInstructions(updateInstructions).setCriteria(rowSelectionCriteria).executeUpdate().getAffectedRows();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }
}
