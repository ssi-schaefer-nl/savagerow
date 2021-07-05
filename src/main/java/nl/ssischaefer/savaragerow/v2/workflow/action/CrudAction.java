package nl.ssischaefer.savaragerow.v2.workflow.action;

import java.util.List;
import java.util.Map;

public abstract class CrudAction extends Action {
    protected String table;
    protected boolean triggerWorkflows;

    public CrudAction() {
    }


    public void perform(Map<String, String> data) {
        List<Map< String, String>> result = null;
        try {
            result = execute(data);
            if(triggerWorkflows)
                triggerWorkflows(result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void triggerWorkflows(List<Map<String, String>> result) {
        if(result == null) return;

        result.forEach(d -> {
            try {
                // trigger other workflows
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected abstract List<Map<String, String>> execute(Map<String, String> data) throws Exception;

    public boolean isTriggerWorkflows() {
        return triggerWorkflows;
    }

    public CrudAction setTriggerWorkflows(boolean triggerWorkflows) {
        this.triggerWorkflows = triggerWorkflows;
        return this;
    }

    public String getTable() {
        return table;
    }

    public CrudAction setTable(String table) {
        this.table = table;
        return this;
    }

}
