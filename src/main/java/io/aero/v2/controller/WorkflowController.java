package io.aero.v2.controller;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.aero.v2.dto.WorkflowOverviewDTO;
import io.aero.v2.model.Workflow;
import io.aero.v2.model.WorkflowType;
import io.aero.v2.model.Workflows;
import io.aero.v2.query.GetTablesQuery;
import io.aero.v2.util.RequestParams;
import io.aero.v2.util.Workspace;

import spark.Request;
import spark.Response;
import spark.Route;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorkflowController {
    private WorkflowController() {
    }

    public static final Route getDbSummary = (Request request, Response response) -> {
        List<String> tables = new GetTablesQuery().execute().getResult();
        Workflows workflows = getWorkflowsFromCurrentWorkspace();
        List<WorkflowOverviewDTO> summary = tables.stream().map(table -> {
            List<Workflow> deleteWorkflows = workflows.getDelete().stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());
            List<Workflow> updateWorkflows = workflows.getUpdate().stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());
            List<Workflow> insertWorkflows = workflows.getInsert().stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());

            return new WorkflowOverviewDTO()
                    .setTable(table)
                    .setDelete((int) deleteWorkflows.stream().filter(Workflow::isActive).count(), deleteWorkflows.size())
                    .setUpdate((int) updateWorkflows.stream().filter(Workflow::isActive).count(), updateWorkflows.size())
                    .setInsert((int) insertWorkflows.stream().filter(Workflow::isActive).count(), insertWorkflows.size());
        }).collect(Collectors.toList());
        return new ObjectMapper().writeValueAsString(summary);
    };

    public static final Route getTableWorkflows = (Request request, Response response) -> {
        String table = request.params(RequestParams.Parameter.Table);
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));

        Workflows workflows = getWorkflowsFromCurrentWorkspace();
        List<Workflow> allWorkflows = new ArrayList<>();

        switch (type) {
            case DELETE:
                allWorkflows = workflows.getDelete();
                break;
            case INSERT:
                allWorkflows = workflows.getInsert();
                break;
            case UPDATE:
                allWorkflows = workflows.getUpdate();
                break;
        }

        List<Workflow> tableWorkflows = allWorkflows.stream().filter(w -> w.getTable().equals(table)).collect(Collectors.toList());
        return new ObjectMapper().writeValueAsString(tableWorkflows);
    };

    public static final Route addWorkflow = (Request request, Response response) -> {
        WorkflowType type = WorkflowType.fromString(request.params(RequestParams.Parameter.WorkflowType));

        ObjectMapper objectMapper = new ObjectMapper().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS);
        Workflow workflow = objectMapper.readValue(request.body(), Workflow.class);

        Workflows workflows = getWorkflowsFromCurrentWorkspace();
        switch (type) {
            case DELETE:
                List<Workflow> deleteFlows = workflows.getDelete();
                if(workflowWithNameExists(workflow.getName(), deleteFlows)) throw new Exception("Delete workflow with this name already exists");
                deleteFlows.add(workflow);
                workflows.setDelete(deleteFlows);
                break;
            case INSERT:
                List<Workflow> insertFlows = workflows.getInsert();
                if(workflowWithNameExists(workflow.getName(), insertFlows)) throw new Exception("Insert workflow with this name already exists");
                insertFlows.add(workflow);
                workflows.setInsert(insertFlows);
                break;
            case UPDATE:
                List<Workflow> updateFlows = workflows.getUpdate();
                if(workflowWithNameExists(workflow.getName(), updateFlows)) throw new Exception("Update workflow with this name already exists");
                updateFlows.add(workflow);
                workflows.setUpdate(updateFlows);
                break;
        }

        try(FileWriter file = new FileWriter(Workspace.getCurrentWorkspace() + "workflows.json")) {
            file.write(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(workflows));
            file.flush();
        } catch (Exception e) {
            throw new Exception("Error while saving workflows");
        }
        return "";
    };

    private static Workflows getWorkflowsFromCurrentWorkspace() {
        try (FileReader reader = new FileReader(Workspace.getCurrentWorkspace() + "workflows.json")) {
            return new ObjectMapper().readValue(reader, Workflows.class);
        } catch (Exception e) {
            return new Workflows();
        }
    }

    private static boolean workflowWithNameExists(String name, List<Workflow> workflows) {
        return workflows.stream().anyMatch(w -> w.getName().equals(name));
    }
}
