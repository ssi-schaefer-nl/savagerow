package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.scheduledworkflow.ScheduledWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

public class WorkflowsDAO {
    private List<TriggeredWorkflow> triggered;
    private List<ScheduledWorkflow> scheduled;

    public List<TriggeredWorkflow> getTriggered() {
        return triggered;
    }

    public WorkflowsDAO setTriggered(List<TriggeredWorkflow> triggered) {
        this.triggered = triggered;
        return this;
    }

    public List<ScheduledWorkflow> getScheduled() {
        return scheduled;
    }

    public WorkflowsDAO setScheduled(List<ScheduledWorkflow> scheduled) {
        this.scheduled = scheduled;
        return this;
    }

    public void save(Path location) throws Exception, WorkspaceNotSetException {
        try (FileWriter file = new FileWriter(location.toString())) {
            file.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(this));
            file.flush();
        } catch (Exception e) {
            throw new Exception("Error while saving workflows");
        }
    }

    public WorkflowsDAO load(Path location) throws WorkspaceNotSetException {
        try (FileReader reader = new FileReader(location.toString())) {
            WorkflowsDAO workflows = new ObjectMapper().readValue(reader, WorkflowsDAO.class);
            setScheduled(workflows.getScheduled());
            setTriggered(workflows.getTriggered());
        } catch (Exception e) {
            return this;
        }
        return this;
    }
}
