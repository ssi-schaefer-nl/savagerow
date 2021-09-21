package nl.ssischaefer.savaragerow.workflow.task.decision;

public class DecisionCriterion {
    private String value1;
    private String comparison;
    private String value2;

    public String getValue1() {
        return value1;
    }

    public String getComparison() {
        return comparison;
    }

    public String getValue2() {
        return value2;
    }

    public DecisionCriterion(String value1, String comparison, String value2) {
        this.value1 = value1;
        this.comparison = comparison;
        this.value2 = value2;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public boolean evaluate() {
        switch(comparison) {
            case "equals": return value1.equalsIgnoreCase(value2);
            case "not equals": return !value1.equalsIgnoreCase(value2);
            case "greater than": return Long.parseLong(value1) > Long.parseLong(value2);
            case "smaller than": return Long.parseLong(value1) < Long.parseLong(value2);
            default: return true;
        }
    }
}
