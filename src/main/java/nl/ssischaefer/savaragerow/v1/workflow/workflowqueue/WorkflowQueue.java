package nl.ssischaefer.savaragerow.v1.workflow.workflowqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class WorkflowQueue {
    protected BlockingQueue<WorkflowTask> queue;
    private ExecutorService executor = null;
    private static int workerCount = 1;
    private static WorkflowQueue workflowTaskQueue = null;

    public static WorkflowQueue getQueue() {
        if(workflowTaskQueue == null) {
            workflowTaskQueue = new WorkflowQueue();
            workflowTaskQueue.start();
        }
        return workflowTaskQueue;
    }

    private WorkflowQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public static void setWorkerCount(int workerCount) {
        WorkflowQueue.workerCount = workerCount;
    }

    public boolean start() {
        if (executor != null || workerCount == 0) return false;
        executor = Executors.newFixedThreadPool(workerCount);
        IntStream.range(0, workerCount).forEach(i -> executor.submit(new WorkflowTaskConsumer(queue)));
        return true;
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
