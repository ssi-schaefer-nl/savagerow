package nl.ssischaefer.savaragerow.v2.workflow.queue;

public class EventConsumer {
    private EventQueue triggerQueue;

    public EventConsumer(EventQueue triggerQueue) {
        this.triggerQueue = triggerQueue;
    }
}
