package nl.ssischaefer.savaragerow.v2.workflow.queue;

import java.util.Map;

public interface Event {
    public String getIdentfier();
    public Map<String, String> getData();
    public Map<String, String> getConfiguration();

}


