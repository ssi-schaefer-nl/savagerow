package nl.ssischaefer.savaragerow.v2.workflow.workflowqueue;

import nl.ssischaefer.savaragerow.v2.workflow.WorkflowsManager;
import nl.ssischaefer.savaragerow.v2.util.exception.WorkspaceNotSetException;

import java.util.concurrent.BlockingQueue;

public class WorkflowTaskConsumer implements Runnable {
    private final BlockingQueue<WorkflowTask> queue;

    public WorkflowTaskConsumer(BlockingQueue<WorkflowTask> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while(!Thread.currentThread().isInterrupted()) {
            WorkflowTask task = null;
            try {
                task = queue.take();
                try {
                    handle(task);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(WorkflowTask task) {
        try {
            WorkflowsManager  workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
            workflows.execute(task.getType(), task.getTable(), task.getData());
        } catch (WorkspaceNotSetException e) {
            e.printStackTrace();
        }
    }
}
