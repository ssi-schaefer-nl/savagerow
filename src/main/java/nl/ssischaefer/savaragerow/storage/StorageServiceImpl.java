package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.storage.common.model.SQLColumn;
import nl.ssischaefer.savaragerow.storage.query.*;

import java.util.*;
import java.util.stream.Collectors;

public class StorageServiceImpl implements StorageService {
    public List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria) {
        try {
            return new FindRowQuery().setTable(table).setCriteria(rowSelectionCriteria).executeQuery().getResult();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<String> getSchema(String table) {
        try {
            return new GetColumnsQuery().setTable(table).execute().stream().map(SQLColumn::getName).collect(Collectors.toList());
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
