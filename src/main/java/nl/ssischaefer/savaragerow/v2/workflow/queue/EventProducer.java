package nl.ssischaefer.savaragerow.v2.workflow.queue;

public class EventProducer {
    private EventQueue triggerQueue;

    public EventProducer(EventQueue triggerQueue) {
        this.triggerQueue = triggerQueue;
    }


    public void produce(Event event) {

        // publish event to queue
    }
}
