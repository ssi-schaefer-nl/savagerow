package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;

import java.util.concurrent.BlockingQueue;

public class LocalTableEventProducer implements TableEventProducer {
    private final BlockingQueue<TableEvent> tableEventQueue;

    public LocalTableEventProducer(BlockingQueue<TableEvent> tableEventQueue) {
        this.tableEventQueue = tableEventQueue;
    }

    public void produce(TableEvent task) {
        tableEventQueue.add(task);
    }
}
