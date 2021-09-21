package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;

public interface TableEventProducer {
    void produce(TableEvent task);
}
