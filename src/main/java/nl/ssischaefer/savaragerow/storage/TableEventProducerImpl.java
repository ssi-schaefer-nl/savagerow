package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class TableEventProducerImpl implements TableEventProducer {
    private final Logger logger = LoggerFactory.getLogger("StorageOperationsController");

    private final BlockingQueue<TableEvent> tableEventQueue;

    public TableEventProducerImpl(BlockingQueue<TableEvent> tableEventQueue) {
        this.tableEventQueue = tableEventQueue;
    }

    public void produce(TableEvent event) {
        logger.info(String.format("Producing table event for table %s and event %s", event.getTable(), event.getType()));
        tableEventQueue.add(event);
    }
}
