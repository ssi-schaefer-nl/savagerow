package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import nl.ssischaefer.savaragerow.storage.StorageService;

import java.util.List;
import java.util.Map;

public class StorageAdapterImpl implements StorageAdapter {
    private StorageService storageService;

    public StorageAdapterImpl(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<String> getTableColumns(String table) {
        return storageService.getSchema(table);
    }

    @Override
    public List<Map<String, String>> get(String table, List<RowSelectionCriterion> rowSelectionCriteria) {
        return storageService.get(table, rowSelectionCriteria);
    }

    @Override
    public void delete(String table, List<RowSelectionCriterion> rowSelectionCriteria) {
        storageService.delete(table, rowSelectionCriteria);
    }

    @Override
    public Long insert(String table, Map<String, String> row) {
        return storageService.insert(table, row);
    }

    @Override
    public void update(String table, List<RowSelectionCriterion> rowIdSelectionCriterion, List<UpdateInstruction> updateInstructions) {
        storageService.update(table, rowIdSelectionCriterion, updateInstructions);
    }
}
