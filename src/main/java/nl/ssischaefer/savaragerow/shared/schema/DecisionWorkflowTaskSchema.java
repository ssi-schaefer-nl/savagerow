package nl.ssischaefer.savaragerow.shared.schema;

import java.util.List;

public class DecisionWorkflowTaskSchema extends AbstractWorkflowTaskSchema {
    private String subtype;
    private List<DecisionCriterionSchema> criteria;
    private Long trueroute;
    private Long falseroute;

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }


    public Long getTrueroute() {
        return trueroute;
    }

    public void setTrueroute(Long trueroute) {
        this.trueroute = trueroute;
    }

    public Long getFalseroute() {
        return falseroute;
    }

    public void setFalseroute(Long falseroute) {
        this.falseroute = falseroute;
    }

    public List<DecisionCriterionSchema> getCriteria() {
        return criteria;
    }

    public void setCriteria(List<DecisionCriterionSchema> criteria) {
        this.criteria = criteria;
    }
}
