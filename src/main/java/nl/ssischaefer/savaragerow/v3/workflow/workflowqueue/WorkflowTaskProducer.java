package nl.ssischaefer.savaragerow.v3.workflow.workflowqueue;

public class WorkflowTaskProducer {
    private final WorkflowTaskQueue workflowTaskQueue;

    public WorkflowTaskProducer(WorkflowTaskQueue workflowTaskQueue) {
        this.workflowTaskQueue = workflowTaskQueue;
    }

    public void produce(WorkflowTask task) {
        workflowTaskQueue.feed(task);
    }
}
