package nl.ssischaefer.savaragerow.workflow.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = EmailAction.class, name = "email"),
        @JsonSubTypes.Type(value = InsertAction.class, name = "insert"),
        @JsonSubTypes.Type(value = UpdateAction.class, name = "update"),
        @JsonSubTypes.Type(value = DeleteAction.class, name = "delete"),
        @JsonSubTypes.Type(value = APICallAction.class, name = "api"),

})
public abstract class Action {
    private int step;
    private String name;

    public String getName() {
        return name;
    }

    public Action setName(String name) {
        this.name = name;
        return this;
    }

    public abstract void perform(Map<String, String> data);

    public int getStep() {
        return step;
    }

    public Action setStep(int step) {
        this.step = step;
        return this;
    }
}
