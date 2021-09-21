package nl.ssischaefer.savaragerow.workflow.mapper;

import nl.ssischaefer.savaragerow.shared.schema.DecisionCriterionSchema;
import nl.ssischaefer.savaragerow.shared.schema.DecisionWorkflowTaskSchema;
import nl.ssischaefer.savaragerow.workflow.task.AbstractWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.task.decision.AbstractDecisionWorkflowTask;
import nl.ssischaefer.savaragerow.workflow.task.decision.DecisionCriterion;
import nl.ssischaefer.savaragerow.workflow.task.decision.InputDecisionTask;


import java.util.List;
import java.util.stream.Collectors;

public class DecisionTaskSchemaMapper {
    public AbstractWorkflowTask mapSchemaToTask(DecisionWorkflowTaskSchema taskSchema) {
        AbstractDecisionWorkflowTask task = null;

        switch (taskSchema.getSubtype()) {
            case "input":
                task = new InputDecisionTask();
                ((InputDecisionTask) task).setCriterion(mapDecisionCriteria(taskSchema.getCriteria()));
                break;
            default:
                return null;
        }
        task.setIfTrue(taskSchema.getTrueroute());
        task.setIfFalse(taskSchema.getFalseroute());
        task.setId(taskSchema.getId());
        return task;
    }

    private List<DecisionCriterion> mapDecisionCriteria(List<DecisionCriterionSchema> criteriaSchema) {
        return criteriaSchema.stream().map(cs ->
                new DecisionCriterion(cs.getValue1(), cs.getComparison(), cs.getValue2())
            ).collect(Collectors.toList());
    }
}
