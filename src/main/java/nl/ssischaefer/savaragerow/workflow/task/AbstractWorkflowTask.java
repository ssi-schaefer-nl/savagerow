package nl.ssischaefer.savaragerow.workflow.task;

import nl.ssischaefer.savaragerow.workflow.exception.WorkflowTaskExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public abstract class AbstractWorkflowTask {
    private static final Logger logger = LoggerFactory.getLogger("WorkflowTask");
    private Long id;
    /***
     * Map original parameter name to new name
     */
    private Map<String, String> propagatedParameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkflowTaskInput execute(WorkflowTaskInput input)  {
        Map<String, String> output = this.performTask(input.getData());
        if (propagatedParameters != null)
            propagatedParameters.keySet().forEach(p -> output.put(propagatedParameters.get(p), input.getData().get(p)));

        return new WorkflowTaskInput(output);
    }

    protected abstract Map<String, String> performTask(Map<String, String> input);

    public abstract Long getNext();

    public Map<String, String> getOutput() {
        Map<String, String> outputParameters = getOutputParameters();
        if (propagatedParameters != null)
            propagatedParameters.values().forEach(p -> outputParameters.put(p, String.format("${%s}", p)));
        return outputParameters;
    }

    protected abstract Map<String, String> getOutputParameters();

    public void setPropagatedParameters(Map<String, String> propagatedParameters) {
        this.propagatedParameters = propagatedParameters;
    }

}
