package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;
import nl.ssischaefer.savaragerow.shared.model.RowSelectionCriterion;
import nl.ssischaefer.savaragerow.shared.model.UpdateInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StorageOperationsController {
    private final Logger logger = LoggerFactory.getLogger("StorageOperationsController");

    private final TableEventProducerImpl taskProducer;
    private final StorageService repository;

    public StorageOperationsController(TableEventProducerImpl taskProducer, StorageService repository) {
        this.taskProducer = taskProducer;
        this.repository = repository;
    }

    public void update(String table, Map<String, String> row, Long rowid) {
        logger.info(String.format("Processing update on table %s for row with id %d", table, rowid));
        Map<String, String> workflowData = new HashMap<>();

        List<UpdateInstruction> instructions = row.keySet().stream()
                .map(field -> new UpdateInstruction(field, "set", row.get(field)))
                .collect(Collectors.toList());

        List<Map<String, String>> oldRows = repository.get(table,  getRowIdSelectionCriterion(rowid));
        int affected = repository.update(table,  getRowIdSelectionCriterion(rowid), instructions);
        List<Map<String, String>> newRows = repository.get(table,  getRowIdSelectionCriterion(rowid));

        if(affected > 0) {
            oldRows.get(0).forEach((key, value) -> workflowData.put("old." + key, value));
            newRows.get(0).forEach((key, value) -> workflowData.put("new." + key, value));
            var event = new TableEvent(table, workflowData, "update");
            taskProducer.produce(event);
        }
    }


    public Map<String, String> insert(String table, Map<String, String>  row) {
        logger.info(String.format("Processing insert on table %s", table));

        Long rowid = repository.insert(table, row);
        List<Map<String, String>> rows = repository.get(table, getRowIdSelectionCriterion(rowid));
        if(!rows.isEmpty()) {
            Map<String, String> res = rows.get(0);
            var event = new TableEvent(table, res, "insert");
            taskProducer.produce(event);
            return res;
        }

        return new HashMap<>();
    }

    public void delete(String table, Long rowid) {
        logger.info(String.format("Processing delete on table %s for row with id %d", table, rowid));

        List<Map<String, String>> getResult = repository.get(table, getRowIdSelectionCriterion(rowid));
        if(!getResult.isEmpty()) {
            Map<String, String> deletedRow = getResult.get(0);
            repository.delete(table, getRowIdSelectionCriterion(rowid));
            var event = new TableEvent(table, deletedRow, "delete");
            taskProducer.produce(event);
        }
    }

    public List<Map<String, String>> get(String table) {
        return repository.getAll(table);
    }

    public List<Map<String, String>> get(String table, Long rowId) {
        return repository.get(table, getRowIdSelectionCriterion(rowId));
    }

    public List<RowSelectionCriterion> getRowIdSelectionCriterion(Long rowid) {
        return Collections.singletonList(RowSelectionCriterion.getRowIdCriterion(rowid));
    }

}
