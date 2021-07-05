package nl.ssischaefer.savaragerow.v2.savaragerow.workflows.workflowqueue;

import java.util.concurrent.BlockingQueue;

public class WorkflowTaskConsumer implements Runnable {
    private final BlockingQueue<WorkflowTask> queue;

    public WorkflowTaskConsumer(BlockingQueue<WorkflowTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            WorkflowTask task = null;
            try {
                task = queue.take();
                task.execute();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
