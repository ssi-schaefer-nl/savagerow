package nl.ssischaefer.savaragerow.workflow.v2.nodes;

public abstract class WorkflowRootNode extends WorkflowNode {
    private WorkflowChildNode next;

    public void execute() {
        return;
    }
}
