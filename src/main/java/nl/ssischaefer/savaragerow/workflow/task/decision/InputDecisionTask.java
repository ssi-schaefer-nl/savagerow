package nl.ssischaefer.savaragerow.workflow.task.decision;

import org.apache.commons.text.StringSubstitutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InputDecisionTask extends AbstractDecisionWorkflowTask {
    private List<DecisionCriterion> criterion;
    private Long next;

    @Override
    protected Map<String, String> performTask(Map<String, String> input) {
        List<DecisionCriterion> criteria = getCriterion(input);
        if(criteria.stream().allMatch(DecisionCriterion::evaluate)) next = ifTrue;
        else next = ifFalse;

        return new HashMap<>();
    }

    @Override
    public Long getNext() {
        return next;
    }

    @Override
    public Map<String, String> getOutputParameters() {
        return new HashMap<>();
    }

    public void setCriterion(List<DecisionCriterion> criterion) {
        this.criterion = criterion;
    }

    private List<DecisionCriterion> getCriterion(Map<String, String> input) {
        var sub = new StringSubstitutor(input, "${", "}");
        return criterion.stream()
                .map(c -> new DecisionCriterion(sub.replace(c.getValue1()), c.getComparison(), sub.replace(c.getValue2())))
                .collect(Collectors.toList());
    }
}
