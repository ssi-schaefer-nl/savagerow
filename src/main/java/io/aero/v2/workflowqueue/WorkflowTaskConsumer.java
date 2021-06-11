package io.aero.v2.workflowqueue;

import io.aero.v2.model.WorkflowsManager;

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
                    queue.offer(task);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handle(WorkflowTask task) {
        WorkflowsManager workflows = WorkflowsManager.getWorkflowsFromCurrentWorkspace();
        workflows.execute(task.getType(), task.getTable(), task.getData());
    }
}
