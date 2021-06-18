package nl.ssischaefer.savaragerow.v2.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.ssischaefer.savaragerow.v2.util.Workspace;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;
import java.util.stream.Collectors;

public class WorkflowsManager {
    private static WorkflowsManager cachedWorkflows = null;
    private static String lastWorkspace = "";
    private final EnumMap<WorkflowType, List<Workflow>> workflows;

    public static WorkflowsManager getWorkflowsFromCurrentWorkspace() {
        String currentWorkspace = Workspace.getCurrentWorkspace();
        // Either we did not cache anything, or workspace changed since last time and we need to update
        if(cachedWorkflows == null || !lastWorkspace.equals(currentWorkspace)) {
            try (FileReader reader = new FileReader(Workspace.getCurrentWorkspace() + "workflows.json")) {
                cachedWorkflows = new ObjectMapper().readValue(reader, WorkflowsManager.class);
                lastWorkspace = currentWorkspace;
            } catch (Exception e) {
                return new WorkflowsManager();
            }
        }
        return cachedWorkflows;
    }

    public Map<WorkflowType, List<Workflow>> getWorkflows() {
        return workflows;
    }

    private WorkflowsManager() {
        workflows = new EnumMap<>(WorkflowType.class);
        Arrays.stream(WorkflowType.values()).forEach(t -> workflows.put(t, new ArrayList<>()));
    }

    public void execute(WorkflowType type, String table, Map<String, String> data) {
        this.workflows.getOrDefault(type, new ArrayList<>()).stream().filter(w -> (w.isActive() && w.getTable().equals(table))).forEach(w -> w.execute(data));
    }

    public List<Workflow> get() {
        return workflows.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Workflow> get(WorkflowType type) {
        return workflows.get(type);
    }

    public List<Workflow> getByTable(WorkflowType type, String table) {
        return get(type).stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());
    }

    public void set(WorkflowType type, List<Workflow> workflows) {
        this.workflows.put(type, workflows);
    }

    public void setActive(WorkflowType type, String table, String name, boolean active) {
        List<Workflow> updatedWorkflows = get(type).stream().peek(w -> {
            if(w.getTable().equals(table) && w.getName().equals(name)) w.setActive(active);
        }).collect(Collectors.toList());

        set(type, updatedWorkflows);
    }


    public void delete(WorkflowType type, String table, String name) {
        List<Workflow> resultOfDeletion = get(type).stream()
                .filter(w -> !(w.getName().equals(name) && w.getTable().equals(table)))
                .collect(Collectors.toList());

        set(type, resultOfDeletion);
    }

    public boolean add(WorkflowType type, Workflow workflow) {
        List<Workflow> temp = this.workflows.getOrDefault(type, new ArrayList<>());
        boolean alreadyExists = getByTable(type, workflow.getTable()).stream().anyMatch(w -> w.getName().equals(workflow.getName()));
        if(!alreadyExists) {
            temp.add(workflow);
            set(type, temp);
        }
        return !alreadyExists;
    }

    public static void save(WorkflowsManager workflows) throws Exception {
        try(FileWriter file = new FileWriter(Workspace.getCurrentWorkspace() + "workflows.json")) {
            file.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflows));
            file.flush();
            cachedWorkflows = workflows;
        } catch (Exception e) {
            throw new Exception("Error while saving workflows");
        }
    }

}
