package nl.ssischaefer.savaragerow.v2.workflow.triggered;
import nl.ssischaefer.savaragerow.v2.workflow.Workflow;

import java.util.Map;

public class TriggeredWorkflow extends Workflow {
    private String table;
    private Type type;

    @Override
    protected void executeWorkflow(Map<String, String> data) {

    }

    public enum Type {
        UPDATE("update"),
        DELETE("delete"),
        INSERT("insert");

        private final String type;

        Type(String type) {
            this.type = type;
        }

        public String getType() {
            return this.type;
        }

        public static Type fromString(String type) {
            for (Type t : Type.values()) {
                if (t.type.equalsIgnoreCase(type)) {
                    return t;
                }
            }
            return null;
        }

    }
}
