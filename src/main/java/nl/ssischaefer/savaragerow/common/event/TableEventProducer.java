package nl.ssischaefer.savaragerow.common.event;

import nl.ssischaefer.savaragerow.common.event.TableEvent;

import java.util.concurrent.BlockingQueue;

public class TableEventProducer {
    private final BlockingQueue<TableEvent> tableEventQueue;

    public TableEventProducer(BlockingQueue<TableEvent> tableEventQueue) {
        this.tableEventQueue = tableEventQueue;
    }

    public void produce(TableEvent task) {
        tableEventQueue.add(task);
    }
}
