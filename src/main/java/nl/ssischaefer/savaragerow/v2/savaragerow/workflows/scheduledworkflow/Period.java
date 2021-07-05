package nl.ssischaefer.savaragerow.v2.savaragerow.workflows.scheduledworkflow;

import java.util.List;

public class Period {
    String time;
    List<String> days;

    public List<String> getDays() {
        return days;
    }

    public Period setDays(List<String> days) {
        this.days = days;
        return this;
    }

    public String getTime() {
        return time;
    }

    public Period setTime(String time) {
        this.time = time;
        return this;
    }
}
