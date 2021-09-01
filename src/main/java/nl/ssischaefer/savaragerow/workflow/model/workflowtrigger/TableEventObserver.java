package nl.ssischaefer.savaragerow.workflow.model.workflowtrigger;

import nl.ssischaefer.savaragerow.common.event.TableEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

public class TableEventObserver implements Runnable {
    private final BlockingQueue<TableEvent> queue;
    private final Map<String, TableTrigger> triggers;

    public TableEventObserver(BlockingQueue<TableEvent> queue) {
        triggers = new HashMap<>();
        this.queue = queue;
    }

    public void addTrigger(TableTrigger trigger) {
        triggers.put(trigger.getWorkflowId(), trigger);
    }

    public void removeTrigger(String id) {
        triggers.remove(id);
    }

    @Override
    public void run() {
        while (true) {
            TableEvent task = null;
            try {
                task = queue.take();
                handle(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(TableEvent task) {
        triggers.values().stream()
                .filter(t -> t.getTable().equalsIgnoreCase(task.getTable()) && t.getEvent().equalsIgnoreCase(task.getType()))
                .forEach(t -> t.trigger(task.getData()));
    }

}
