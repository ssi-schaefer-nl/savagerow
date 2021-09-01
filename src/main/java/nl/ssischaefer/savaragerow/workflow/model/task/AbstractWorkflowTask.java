package nl.ssischaefer.savaragerow.workflow.model.task;

import java.util.Map;

public abstract class AbstractWorkflowTask {
    private Long id;
    private AbstractWorkflowTask next;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AbstractWorkflowTask getNext() {
        return next;
    }

    public void setNext(AbstractWorkflowTask next) {
        this.next = next;
    }


    public void execute(Map<String, String> input) {
        Map<String, String> output = this.performTask(input);
        if(next != null)
            next.execute(output);
    }

    protected abstract Map<String, String> performTask(Map<String, String> input);

}
