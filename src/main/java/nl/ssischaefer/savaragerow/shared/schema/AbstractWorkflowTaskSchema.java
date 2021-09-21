package nl.ssischaefer.savaragerow.shared.schema;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "taskType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CrudWorkflowTaskSchema.class, name = "crud"),
        @JsonSubTypes.Type(value = DecisionWorkflowTaskSchema.class, name = "decision"),

})
public abstract class AbstractWorkflowTaskSchema {
    private String name;
    private Long id;
    private List<Long> neighbors;
    private String taskType;
    private Map<String, String> propagatedParameters;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Long> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Long> neighbors) {
        this.neighbors = neighbors;
    }

    public Map<String, String> getPropagatedParameters() {
        return propagatedParameters;
    }

    public void setPropagatedParameters(Map<String, String> propagatedParameters) {
        this.propagatedParameters = propagatedParameters;
    }
}
