package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;
import nl.ssischaefer.savaragerow.workflow.trigger.TableEventTrigger;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.stream.Collectors;

public class TableEventConsumer implements Runnable {
    private final Logger logger = LoggerFactory.getLogger("TableEventObserver");

    private final BlockingQueue<TableEvent> queue;
    private List<TableEventTrigger> triggers;
    private final WorkflowScheduler workflowScheduler;

    public TableEventConsumer(BlockingQueue<TableEvent> queue, WorkflowScheduler workflowScheduler) {
        this.workflowScheduler = workflowScheduler;
        triggers = new ArrayList<>();
        this.queue = queue;
    }

    public void addTrigger(TableEventTrigger trigger) {
        logger.info(String.format("Added table event trigger for workflow %s", trigger.getWorkflow()));
        triggers.add(trigger);
    }

    public void removeTrigger(String id) {
        triggers = triggers.stream().filter(t -> !t.getWorkflow().equalsIgnoreCase(id)).collect(Collectors.toList());
        logger.info(String.format("Removed table event trigger for workflow %s", id));

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
                logger.error(e.getMessage());
            }
        }
    }

    private void handle(TableEvent event) {
        logger.info(String.format("Handling %s event on table %s", event.getTable(), event.getType()));

        triggers.stream()
                .filter(t -> t.isTriggered(event.getTable(), event.getType()))
                .forEach(t -> {
                    try {
                        workflowScheduler.triggerWorkflow(t.getWorkflow(), event.getData());
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage());
                    }
                });
    }

}
