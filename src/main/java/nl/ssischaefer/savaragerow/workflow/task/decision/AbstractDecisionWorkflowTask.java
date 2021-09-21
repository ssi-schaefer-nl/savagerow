package nl.ssischaefer.savaragerow.workflow.task.decision;

import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;

public abstract class AbstractDecisionWorkflowTask extends AbstractWorkflowTask {
    protected Long ifTrue;
    protected Long ifFalse;

    public void setIfTrue(Long ifTrue) {
        this.ifTrue = ifTrue;
    }

    public void setIfFalse(Long ifFalse) {
        this.ifFalse = ifFalse;
    }
}
