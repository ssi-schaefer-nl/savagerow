package nl.ssischaefer.savaragerow.v3.workflow.workflowqueue;

import nl.ssischaefer.savaragerow.v3.workflow.WorkflowService;
import nl.ssischaefer.savaragerow.v3.workflow.exceptions.WorkflowTaskQueueException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class WorkflowTaskQueue {
    protected BlockingQueue<WorkflowTask> queue;
    private ExecutorService executor = null;
    private static int workerCount = 1;
    private static WorkflowTaskQueue workflowTaskQueue = null;

    private WorkflowTaskQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public static WorkflowTaskQueue initQueue(WorkflowService workflowService) {
        if(workflowTaskQueue == null) {
            workflowTaskQueue = new WorkflowTaskQueue();
            workflowTaskQueue.startWorkers(workflowService);
        }
        return workflowTaskQueue;
    }

    public static WorkflowTaskQueue getQueue() throws WorkflowTaskQueueException {
        if(workflowTaskQueue == null) {
            throw new WorkflowTaskQueueException("Not initialized");
        }
        return workflowTaskQueue;
    }

    public static void setWorkerCount(int workerCount) {
        WorkflowTaskQueue.workerCount = workerCount;
    }

    private void startWorkers(WorkflowService workflowService) {
        if (executor != null) return;
        executor = Executors.newFixedThreadPool(workerCount);
        IntStream.range(0, workerCount).forEach(i -> executor.submit(new WorkflowTaskConsumer(queue, workflowService)));
    }

    public boolean stop() {
        if (executor == null) return false;
        executor.shutdownNow();
        return true;
    }

    public void feed(WorkflowTask task) {
        queue.add(task);
    }
}
