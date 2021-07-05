package nl.ssischaefer.savaragerow.v2.workflow.queue;

import java.util.Map;

public class TriggerEvent implements Event {

    public static Event create(String identifier, Map<String, String> data) {
        return new TriggerEvent();
    }

    @Override
    public String getIdentfier() {
        return null;
    }

    @Override
    public Map<String, String> getData() {
        return null;
    }

    @Override
    public Map<String, String> getConfiguration() {
        return null;
    }
}
