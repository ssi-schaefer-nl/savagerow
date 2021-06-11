package io.aero.v2.workflowqueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.IntStream;

public class WorkflowTaskQueue {
    protected BlockingQueue<WorkflowTask> queue;
    private ExecutorService executor = null;
    private final int workerCount;

    public WorkflowTaskQueue(int workerCount) {
        this.workerCount = workerCount;
        queue = new LinkedBlockingQueue<>();
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
