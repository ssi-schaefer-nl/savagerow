package nl.ssischaefer.savaragerow.v2.workflowqueue;

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

    public static WorkflowTaskQueue getQueue() {
        if(workflowTaskQueue == null) {
            workflowTaskQueue = new WorkflowTaskQueue();
            workflowTaskQueue.start();
        }
        return workflowTaskQueue;
    }

    private WorkflowTaskQueue() {
        queue = new LinkedBlockingQueue<>();
    }

    public static void setWorkerCount(int workerCount) {
        WorkflowTaskQueue.workerCount = workerCount;
    }

    public boolean start() {
        if (executor != null) return false;
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
