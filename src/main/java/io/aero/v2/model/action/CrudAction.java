package io.aero.v2.model.action;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "crudType")
@JsonSubTypes({
        @JsonSubTypes.Type(value = InsertAction.class, name = "insert"),
})
public abstract class CrudAction extends Action {

}
