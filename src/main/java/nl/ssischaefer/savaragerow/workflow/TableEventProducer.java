package nl.ssischaefer.savaragerow.workflow;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;

public interface TableEventProducer {
    void produce(TableEvent task);
}
