package nl.ssischaefer.savaragerow.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.util.Workspace;
import nl.ssischaefer.savaragerow.util.exception.WorkspaceNotSetException;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.TriggeredWorkflow;
import nl.ssischaefer.savaragerow.workflow.triggeredworkflow.WorkflowType;

import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class WorkflowsManager {
    private static WorkflowsManager cachedWorkflows = null;
    private static String lastWorkspace = "";
    private final EnumMap<WorkflowType, List<AbstractWorkflow>> workflows;

    public static WorkflowsManager getWorkflowsFromCurrentWorkspace() throws WorkspaceNotSetException {
        String currentWorkspace = Workspace.getCurrentWorkspace();
        // Either we did not cache anything, or workspace changed since last time and we need to update
        if (cachedWorkflows == null || !lastWorkspace.equals(currentWorkspace)) {
            Path path = Paths.get(Workspace.getCurrentWorkspace() , "workflows.json");
            try (FileReader reader = new FileReader(path.toString())) {
                cachedWorkflows = new ObjectMapper().readValue(reader, WorkflowsManager.class);
                lastWorkspace = currentWorkspace;
            } catch (Exception e) {
                return new WorkflowsManager();
            }
        }
        return cachedWorkflows;
    }

    public Map<WorkflowType, List<AbstractWorkflow>> getWorkflows() {
        return workflows;
    }

    private WorkflowsManager() {
        workflows = new EnumMap<>(WorkflowType.class);
        Arrays.stream(WorkflowType.values()).forEach(t -> workflows.put(t, new ArrayList<>()));
    }

    public void execute(WorkflowType type, String table, Map<String, String> data) {
//        this.workflows.getOrDefault(type, new ArrayList<>()).stream().filter(w -> (w.isActive() && w.getTable().equals(table))).forEach(w -> w.execute(data));
    }

    public List<AbstractWorkflow> get() {
        return workflows.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public Object get(WorkflowVariant variant) {
        return null;
    }

    public List<AbstractWorkflow> get(WorkflowType type) {
        return workflows.get(type);
    }

    public List<AbstractWorkflow> get(WorkflowType type, String table) {
//        return get(type).stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());
        return null;
    }


    public void set(WorkflowType type, List<AbstractWorkflow> workflows) {
        this.workflows.put(type, workflows);
    }

    public void setActive(WorkflowType type, String table, String name, boolean active) {
//        List<TriggeredWorkflow> updatedWorkflows = get(type).stream().peek(w -> {
//            if (w.getTable().equals(table) && w.getName().equals(name)) w.setActive(active);
//        }).collect(Collectors.toList());
//
//        set(type, updatedWorkflows);
    }


    public void delete(WorkflowType type, String table, String name) {
//        List<TriggeredWorkflow> resultOfDeletion = get(type).stream()
//                .filter(w -> !(w.getName().equals(name) && w.getTable().equals(table)))
//                .collect(Collectors.toList());
//
//        set(type, resultOfDeletion);
    }

    public void add(WorkflowType type, TriggeredWorkflow workflow) {
//        List<TriggeredWorkflow> temp = this.workflows.getOrDefault(type, new ArrayList<>()).stream().filter(w -> !(w.getTable().equals(workflow.getTable()) && w.getName().equals(workflow.getName()))).collect(Collectors.toList());
//        temp.add(workflow);
//        set(type, temp);
    }


    public static void save(WorkflowsManager workflows) throws Exception, WorkspaceNotSetException {
        Path path = Paths.get(Workspace.getCurrentWorkspace(), "workflows.json");
        try (FileWriter file = new FileWriter(path.toString())) {
            file.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflows));
            file.flush();
            cachedWorkflows = workflows;
        } catch (Exception e) {
            throw new Exception("Error while saving workflows");
        }
    }

}
