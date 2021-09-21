package nl.ssischaefer.savaragerow.storage;

import nl.ssischaefer.savaragerow.shared.event.TableEvent;

public interface EventProducerAdapter {
    void produce(TableEvent tableEvent);
}
