package nl.ssischaefer.savaragerow.v1.workflow;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public abstract class AbstractWorkflow {
    private String name;
    private boolean active;
    private List<TableCondition> conditions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TableCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<TableCondition> conditions) {
        this.conditions = conditions;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    protected boolean conditionsSatisfied(Map<String, String> data) {
        return conditions.stream().allMatch(c -> {
            try {
                return c.isSatisfied(data);
            } catch (SQLException throwables) {
                return false;
            }
        });
    }

    public abstract void execute(Map<String, String> data);
}
