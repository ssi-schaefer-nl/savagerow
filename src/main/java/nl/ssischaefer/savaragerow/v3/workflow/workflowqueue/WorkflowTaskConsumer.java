package nl.ssischaefer.savaragerow.v3.workflow.workflowqueue;

import nl.ssischaefer.savaragerow.v3.workflow.WorkflowService;
import java.util.concurrent.BlockingQueue;

public class WorkflowTaskConsumer implements Runnable {
    private final BlockingQueue<WorkflowTask> queue;
    private final WorkflowService workflowService;

    public WorkflowTaskConsumer(BlockingQueue<WorkflowTask> queue, WorkflowService workflowService) {
        this.queue = queue;
        this.workflowService = workflowService;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            WorkflowTask task = null;
            try {
                task = queue.take();
                handle(task);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(WorkflowTask task) {
        try {
            workflowService.findByTask(task).forEach(workflow -> workflow.execute(task.getData()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
